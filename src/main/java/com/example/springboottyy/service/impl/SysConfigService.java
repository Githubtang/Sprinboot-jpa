package com.example.springboottyy.service.impl;

import com.example.springboottyy.common.constant.CacheConstants;
import com.example.springboottyy.common.constant.UserConstants;
import com.example.springboottyy.common.core.text.Convert;
import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.SysConfig;
import com.example.springboottyy.repository.SysConfigRepository;
import com.example.springboottyy.utils.CacheUtils;
import com.example.springboottyy.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: GithubTang
 * @Description: 参数配置 服务层实现
 * @Date: 2025/3/11 21:25
 * @Version: 1.0
 */
@Service
public class SysConfigService {
    private static final Logger log = LoggerFactory.getLogger(SysConfigService.class);
    @Autowired
    private SysConfigRepository repository;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     *
     * @param id 参数配置ID
     * @return 参数配置信息
     */
    public SysConfig selectConfigById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(getCache().get(configKey, String.class));
        if (StringUtils.isEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        Optional<SysConfig> retConfig = repository.findByConfigKeyOrId(config.getConfigKey(), config.getId());
        if (retConfig.isPresent()) {
            SysConfig sysConfig = retConfig.get();
            CacheUtils.put(CacheConstants.SYS_CONFIG_KEY, configKey, sysConfig.getConfigValue());
            return sysConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        return Convert.toBool(captchaEnabled, true);
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config) {
        return repository.findAll();
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public int insertConfig(SysConfig config) {
        SysConfig save = repository.save(config);
        if (!ObjectUtils.isEmpty(save)) {
            CacheUtils.put(CacheConstants.SYS_CONFIG_KEY, config.getConfigKey(), config.getConfigValue());
            return 1;
        }
        return 0;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public int updateConfig(SysConfig config) {
        Optional<SysConfig> byId = repository.findById(config.getId());
        if (byId.isEmpty()) {
            return 0;
        }
        SysConfig temp = byId.get();
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
            CacheUtils.remove(CacheConstants.SYS_CONFIG_KEY, config.getConfigKey());
        }
        SysConfig save = repository.save(config);
        if (!ObjectUtils.isEmpty(save)) {
            CacheUtils.put(CacheConstants.SYS_CONFIG_KEY, config.getConfigKey(), save.getConfigValue());
            return 1;
        }
        return 0;

    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Transactional
    public void deleteConfigByIds(List<Long> configIds) {
        List<SysConfig> configs = repository.findAllById(configIds);
        ArrayList<Long> delIds = new ArrayList<>();
        for (SysConfig config : configs) {
            if (config.getConfigType().equals(UserConstants.YES)) {
                throw new ServiceException(String.format("系统内置参数【%1$s】不能删除", config.getConfigKey()));
            }
            delIds.add(config.getId());
            getCache().evict(config.getConfigKey());
        }
        repository.deleteAllById(delIds);
    }

    /**
     * 加载参数缓存数据 配置信息
     */
    public void loadingConfigCache() {
        // 获取配置列表
        List<SysConfig> configsList = repository.findAll();
        for (SysConfig config : configsList) {
            getCache().put(config.getConfigKey(), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    public void clearConfigCache() {
        CacheUtils.getCache(CacheConstants.SYS_CONFIG_KEY).clear();
    }

    /**
     * 重置参数缓存数据
     */
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public boolean checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtils.isNull(config.getId()) ? -1L : config.getId();
        Optional<SysConfig> info = repository.findByConfigKey(config.getConfigKey());
        if (info.isPresent() && !Objects.equals(info.get().getId(), configId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    private Cache getCache() {
        return CacheUtils.getCache(CacheConstants.SYS_CONFIG_KEY);
    }
}
