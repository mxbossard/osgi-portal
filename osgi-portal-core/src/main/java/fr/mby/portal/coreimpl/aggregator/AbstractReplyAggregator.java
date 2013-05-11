/**
 * Copyright 2013 Maxime Bossard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.mby.portal.coreimpl.aggregator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.GenericTypeResolver;

import fr.mby.portal.core.aggregator.IReplyAggregator;
import fr.mby.portal.message.IReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AbstractReplyAggregator<T extends IReply> implements IReplyAggregator<T> {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(AbstractReplyAggregator.class);

	/** Subclasses logger. */
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public boolean supportsReplyType(final Class<? extends IReply> replyType) {
		Class<?> typeArg = GenericTypeResolver.resolveTypeArgument(this.getClass(), IReplyAggregator.class);
		if (typeArg == null || typeArg.equals(ApplicationEvent.class)) {
			final Class<?> targetClass = AopUtils.getTargetClass(this);
			if (targetClass != this.getClass()) {
				typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, ApplicationListener.class);
			}
		}

		final boolean test = typeArg == null || typeArg.isAssignableFrom(replyType);

		AbstractReplyAggregator.LOG.debug(
				"IReplyAggregator: [{}] supported type found: [{}] will supports [{}] type ? => [{}].",
				this.getClass(), typeArg, replyType, test);

		return test;
	}

}
