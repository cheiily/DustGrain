package dustgrain.core.cache

import dustgrain.core.Application
import dustgrain.core.config.AppConfig
import dustgrain.core.domain.DataHeader
import dustgrain.core.fetching.DataFetchService
import net.harawata.appdirs.AppDirsFactory

interface DataHeaderCache : SuspendingKVCache<String, List<DataHeader>>

class InMemoryDataHeaderCache(
    dataFetchService: DataFetchService
) : InMemoryKVCache<String, List<DataHeader>>(
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders)
)

class PersistentDataHeaderCache(
    dataFetchService: DataFetchService,
    appConfig: AppConfig = Application.config
) : PersistentKVCache<String, List<DataHeader>>(
    directory = AppDirsFactory.getInstance().getUserCacheDir(
        appConfig.appInfo.name,
        appConfig.appInfo.version + "-c" + appConfig.appInfo.cacheVersion,
        appConfig.appInfo.author
    ),
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders),
    keyCodec = StringCodec(),
    valueCodec = DataHeaderListCodec()
)

class NoopDataHeaderCache(
    dataFetchService: DataFetchService
) : NoopKVCache<String, List<DataHeader>>(
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders)
)