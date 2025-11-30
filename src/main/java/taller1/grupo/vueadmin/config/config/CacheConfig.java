package taller1.grupo.vueadmin.config.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: richard sivila
 * @description: Configuracion del Cache
 * @author: richard sivila
 * @create: 2024
 **/
@Component
public class CacheConfig {

    private Cache<String, String> cache;

    /**
     * cache
     * 
     * @param key
     * @param value
     * @param expireTime
     */
    public void put(String key, String value, int expireTime) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expireTime, TimeUnit.MINUTES).build();
        cache.put(key, value);
    }

    /**
     * conseguir
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * claro
     * 
     * @param key
     */
    public void invalidate(String key) {
        cache.invalidate(key);
    }

    /**
     * Ejecútelo una vez al día a la 1 en punto para borrar el caché caducado
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearExpired() {
        if (cache != null) {
            cache.cleanUp();
        }
    }
}
