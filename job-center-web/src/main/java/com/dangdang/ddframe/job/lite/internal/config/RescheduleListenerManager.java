

package com.dangdang.ddframe.job.lite.internal.config;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.listener.AbstractJobListener;
import com.dangdang.ddframe.job.lite.internal.listener.AbstractListenerManager;
import com.dangdang.ddframe.job.lite.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.lianshang.job.center.web.util.JobUtil;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;

/**
 * 重调度监听管理器
 */
public final class RescheduleListenerManager extends AbstractListenerManager {

	private final ConfigurationNode configNode;

	private final String jobName;

	public RescheduleListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName) {
		super(regCenter, jobName);
		this.jobName = jobName;
		configNode = new ConfigurationNode(jobName);
	}

	@Override
	public void start() {
		addDataListener(new CronSettingAndJobEventChangedJobListener());
	}

	class CronSettingAndJobEventChangedJobListener extends AbstractJobListener {

		@Override
		protected void dataChanged(final String path, final Type eventType, final String data) {
			if (configNode.isConfigPath(path) && Type.NODE_UPDATED == eventType && !JobRegistry.getInstance().isShutdown(jobName)) {
				//配置信息修改,刷新数据库
				LiteJobConfiguration liteJobConfiguration = LiteJobConfigurationGsonFactory.fromJson(data);
				JobTypeConfiguration jobTypeConfiguration =  liteJobConfiguration.getTypeConfig();
				JobCoreConfiguration jobCoreConfiguration = jobTypeConfiguration.getCoreConfig();
				String cron = jobCoreConfiguration.getCron();

				JobRegistry.getInstance().getJobScheduleController(jobName).rescheduleJob(cron);

				JobUtil.freshJobItem(jobCoreConfiguration);
			}
		}
	}
}
