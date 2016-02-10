package gov.hhs.onc.phiz.json.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

public class PhizObjectMapperFactoryBean extends Jackson2ObjectMapperFactoryBean {
    private class PhizPrettyPrinter extends DefaultPrettyPrinter {
        private final static long serialVersionUID = 0L;

        public PhizPrettyPrinter(String rootIndentStr) {
            super(rootIndentStr);
        }

        @Override
        public void writeObjectFieldValueSeparator(JsonGenerator jsonGen) throws IOException {
            jsonGen.writeRaw(FIELD_VALUE_DELIM);
        }

        @Override
        public PhizPrettyPrinter createInstance() {
            return this;
        }
    }

    private final static SerializedString FIELD_VALUE_DELIM = new SerializedString(": ");

    private int indentSize;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        ObjectMapper objMapper = this.getObject();

        if (objMapper.isEnabled(SerializationFeature.INDENT_OUTPUT) && (this.indentSize > 0)) {
            DefaultIndenter indenter = new DefaultIndenter(StringUtils.repeat(StringUtils.SPACE, this.indentSize), StringUtils.LF);

            PhizPrettyPrinter prettyPrinter = new PhizPrettyPrinter(indenter.getIndent());
            prettyPrinter.indentArraysWith(indenter);
            prettyPrinter.indentObjectsWith(indenter);

            objMapper.setDefaultPrettyPrinter(prettyPrinter);
        }
    }

    public void setFeatures(Map<Object, Boolean> features) {
        features.forEach(this::setFeature);
    }

    public void setFeature(Object feature, boolean featureValue) {
        if (featureValue) {
            this.setFeaturesToEnable(feature);
        } else {
            this.setFeaturesToDisable(feature);
        }
    }

    public int getIndentSize() {
        return this.indentSize;
    }

    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize;
    }
}
