/*
        Copyright (c) 2015 King's College London

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.typehandlers.TypeHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DbColumnService implements ApplicationContextAware {

    private ApplicationContext appContext;

    /**
     * @param dbName Database name. E.g. mysql, sqlserver, hsql, oracle etc.
     * @return The type handler for the given database.
     */
    public TypeHandler getDbTypeHandlerForDb(String dbName) {
        Map<String, TypeHandler> beansOfType = appContext.getBeansOfType(TypeHandler.class);
        for (Object bean : beansOfType.values()) {
            TypeHandler handler = (TypeHandler) bean;
            if (handler.canHandle(dbName)) {
                return handler;
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
