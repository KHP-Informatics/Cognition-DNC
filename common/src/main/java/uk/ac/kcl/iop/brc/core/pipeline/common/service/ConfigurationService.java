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

package uk.ac.kcl.iop.brc.core.pipeline.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.util.stream.Collectors.toList;

@Service
public class ConfigurationService {

    @Value("${configPath}")
    private String configFilePath;

    public String getValueAsString(String key) {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return properties.getProperty(key);
    }

    public List<String> getValuesAsList(String key, String separator) {
        String valueAsString = getValueAsString(key);
        String[] values = valueAsString.split(separator);
        List<String> strings = Arrays.asList(values);
        return strings.stream().map(String::trim).collect(toList());
    }

    public Integer getValueAsInteger(String key) {
        String val = getValueAsString(key);
        return Integer.valueOf(val);
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }
}
