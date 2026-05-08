package dustgrain.core.cache

import dustgrain.core.Application
import dustgrain.core.domain.DataHeader
import dustgrain.core.fetching.DataFetchService
import net.harawata.appdirs.AppDirsFactory

typealias DataHeaderCache = SuspendingKVCache<String, List<DataHeader>>

class InMemoryDataHeaderCache(
    dataFetchService: DataFetchService
) : InMemoryKVCache<String, List<DataHeader>>(
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders),
    maxAgeSeconds = Application.config.cache.maxAgeSeconds
)

class PersistentDataHeaderCache(
    dataFetchService: DataFetchService
) : PersistentKVCache<String, List<DataHeader>>(
    directory = AppDirsFactory.getInstance().getUserCacheDir(
        Application.config.appInfo.name,
        Application.config.appInfo.version + "-c" + Application.config.cache.version,
        Application.config.appInfo.author
    ),
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders),
    keyCodec = StringCodec(),
    valueCodec = DataHeaderListCodec(),
    version = Application.config.cache.version,
    maxAgeSeconds = Application.config.cache.maxAgeSeconds
)

class NoopDataHeaderCache(
    dataFetchService: DataFetchService
) : NoopKVCache<String, List<DataHeader>>(
    provider = SuspendingCacheEntryProvider(dataFetchService::getTableHeaders)
)
