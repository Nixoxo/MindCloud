package de.pm.mindcloud.web.domain.response;

/**
 * Created by samuel on 03.07.15.
 */
public class MindCloudMessage {
    public enum Type {
        INFO("info"), SUCCESS("success"), WARNING("warning"), ERROR("error");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String text;
    private Type type;

    public MindCloudMessage(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
