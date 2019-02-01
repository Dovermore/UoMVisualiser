package Subject;

import Util.Constants;
import Util.JSONable;
import org.json.JSONObject;

public class Data implements JSONable {
    private String code;

    public Data(JSONObject jsonObject) {
        code = jsonObject.getString(Constants.JSONKey.CODE);
    }

    public Data(String code) {
        this.code = code;
    }

    @Override
    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * We use code to identify a subject
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    /**
     * We use code to identify a subject
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Data) {
            return ((Data) obj).code.equals(code);
        }
        return false;
    }

    public boolean sameCode(String code) {
        return this.code.equals(code);
    }

    // TODO Implement
    @Override
    public String toString() {
        return String.format("code: %s", code);
    }
}
