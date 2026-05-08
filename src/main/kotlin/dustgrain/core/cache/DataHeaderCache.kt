package dustgrain.core.cache

import dustgrain.core.Application
import dustgrain.core.config.AppConfig
import dustgrain.core.domain.DataHeader
import dustgrain.core.fetching.DataFetchService
import net.harawata.appdirs.AppDirsFactory

typealias DataHeaderCache = SuspendingKVCache<String, List<DataHeader>>

class InMemoryDataHeaderCache(
    dataFetchService: DataFetchService,
    appConfig: AppConfig = Application.config
) : InMemoryKVCache<String, List<DataHeader>>(
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders),
    maxAgeSeconds = appConfig.cache.maxAgeSeconds
)

class PersistentDataHeaderCache(
    dataFetchService: DataFetchService,
    appConfig: AppConfig = Application.config
) : PersistentKVCache<String, List<DataHeader>>(
    directory = AppDirsFactory.getInstance().getUserCacheDir(
        appConfig.appInfo.name,
        appConfig.appInfo.version + "-c" + appConfig.cache.version,
        appConfig.appInfo.author
    ),
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders),
    keyCodec = StringCodec(),
    valueCodec = DataHeaderListCodec(),
    version = appConfig.cache.version,
    maxAgeSeconds = appConfig.cache.maxAgeSeconds
)

class NoopDataHeaderCache(
    dataFetchService: DataFetchService
) : NoopKVCache<String, List<DataHeader>>(
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders)
)