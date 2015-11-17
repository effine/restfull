/**
 * Filename：Message.java Created by: neil Created on: 2008-5-6 下午06:22:06 Last
 * modified by：$Author$ Last modified on: $Date$ Revision: $Revision$
 */
package cn.effine.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import net.sf.json.xml.XMLSerializer;
import cn.effine.util.ValidateUtil;

/*
 * 通信的消息，用于与客户端交互 v1.1 采用json lib 进行标准的json格式 去掉Code enum
 */
public class Response {

    public final static int OK = 200;
    public final static int ERROR = 500;
    public final static int VERIFY = 403;
    public final static int REDIRECT = 302;
    private int code = OK;                           //
    private int version = 2;                            // 版本
    private Map<String, Object> messages = new HashMap<String, Object>();

    public Response() {
    }

    public Response(int code) {
        this.code = code;
    }

    public boolean hasError() {
        return (code != 200) || (code == ERROR & messages != null && messages.size() > 0);
    }

    @SuppressWarnings("unchecked")
    public void addMessage(String id, Object message) {
        if (ValidateUtil.isNull(id) || ValidateUtil.isNull(message)) {
            return;
        }
        //模拟一个多值的Map
        if (messages.get(id) != null) {
            Object v = messages.get(id);
            if (v instanceof ArrayList) {
                ((ArrayList<Object>) v).add(message);
            } else {
                ArrayList n = new ArrayList();
                n.add(v);
                n.add(message);
                messages.put(id, n);
            }
        } else {
            messages.put(id, message);
        }
    }

    public void addError(String errorKey, Object errorMessage) {
        addMessage(errorKey, errorMessage);
        code = ERROR; // 设置为错误
    }

    @Deprecated
    public void addMessage(String id, String message, boolean cdata) {
        addMessage(id, message);

    }

    public void setOk(boolean ok) {
        this.code = ok ? OK : ERROR;
    }

    public boolean isOk() {
        return this.code == OK;
    }

    public boolean isError() {
        return this.code == ERROR;
    }

    public void setError(boolean error) {
        this.code = error ? ERROR : OK;
    }

    public void setRedirect(boolean redirect) {
        this.code = redirect ? REDIRECT : OK;
    }

    public void setRedirect(String url) {
        this.code = REDIRECT;
        addMessage("returnUrl", url);
    }

    public boolean getRedirect() {
        return this.code == REDIRECT;
    }

    public void setVerify(boolean verfify) {
        this.code = verfify ? VERIFY : OK;
    }

    public void setVerify() {
        this.code = VERIFY;
    }

    public boolean isVerify() {
        return this.code == VERIFY;
    }

    private static String toJavaScript(String str) {
        if (ValidateUtil.isNull(str)) {
            return "";
        }
        StringBuffer out = new StringBuffer();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\b':
                    out.append('\\');
                    out.append('b');
                    break;
                case '\n':
                    // out.append('\\');
                    // out.append('n');
                    break;
                case '\t':
                    // out.append('\\');
                    // out.append('t');
                    break;
                case '\f':
                    // out.append('\\');
                    // out.append('f');
                    break;
                case '\r':
                    // out.append('\\');
                    // out.append('r');
                    break;
                case '\'':
                    // out.append('\\');
                    out.append('\\');
                    out.append('\'');
                    break;
                case '"':
                    // out.append('\\');
                    out.append('"');
                    break;
                case '\\':
                    out.append('\\');
                    out.append('\\');
                    break;
                default:
                    out.append(ch);
                    break;
            }
        }
        // str = str.replaceAll("'", "\\'");
        // str = str.replaceAll("\r\n", "");
        // str = str.replaceAll("\n", "");
        // str = str.replaceAll("\r", "");
        // str = str.replaceAll("\t", ""); //转义字符
        return out.toString();
    }

    public JSONObject toJSONObject() {
        return JSONObject.fromObject(this);
    }

    public String toJSON() {
        //转义成标准的JSON格式
        JSONObject jsonObject = toJSONObject();
        return jsonObject.toString();
    }

    // 导出为json格式
    @Deprecated
    @SuppressWarnings("unchecked")
    public String toJson() {
        this.version = 1;
        StringBuffer json = new StringBuffer();
        json.append("[{");

        json.append("code:" + code + ",");
        json.append("version:" + version + ",");
        json.append("messages:[");
        String allkey = "";
        Iterator<String> iterator = messages.keySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object v = messages.get(key);
            if (v instanceof String) {
                json.append("{id:'" + key + "',message:'" + toJavaScript((String) v) + "'},");
                allkey = allkey + "," + key;
                i++;
            }
            if (v instanceof ArrayList) {
                ArrayList a = (ArrayList) v;
                for (int j = 0; j < a.size(); j++) {
                    if (a.get(j) instanceof String) {
                        json.append("{id:'" + key + "',message:'" + toJavaScript((String) a.get(j)) + "'},");
                        allkey = allkey + "," + key;
                        i++;
                    }
                }

            }
        }
        if (messages.size() > 0) {
            // 去掉最后一个,
            json.setLength(json.length() - 1);
        }
        json.append("]");
        // 只有出错
        if (code == ERROR && i > 0) {
            json.append(",errorKeys:'" + allkey.substring(1) + "'");// 去掉,
        }
        json.append("}]");
        return json.toString();
    }

    public String getStringMessage(String key) {
        Object v = messages.get(key);
        if (v instanceof ArrayList) {
            ArrayList<String> list = (ArrayList<String>) v;
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        if (v instanceof String) {
            return (String) v;
        }
        return null;
    }

    public String[] getStringMessages(String key) {
        Object v = messages.get(key);
        if (v instanceof ArrayList) {
            ArrayList<String> list = (ArrayList<String>) v;
            return list.toArray(new String[list.size()]);
        }
        if (v instanceof String) {
            return new String[]{(String) v};
        }
        return new String[0];
    }

    public Map<String, Object> getMessages() {
        return messages;
    }

    // 导出为xml格式
    public String toXml() {
        JSONObject jsonObject = toJSONObject();
        return new XMLSerializer().write(jsonObject);
    }

    /**
     * 返回js格式的形式，主要用于跨域调用
     * @return
     */
    public String toJs(String callback) {
        if (ValidateUtil.isNull(callback)) {
            callback = "jsCallBack";
        }
        StringBuffer js = new StringBuffer();
        String json = toJSON();
        js.append("if(" + callback + "){\n" + callback + "(" + json + ");\n}");
        return js.toString();
    }

    //-----------------
    public static Response valueOf(String content) throws JSONException {
        try {
            JSONObject jsonObject = toJSONObject(content);
            Response response = (Response) JSONObject.toBean(jsonObject,
                    Response.class);
            response.setCode(jsonObject.getInt("code"));
            return response;
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    private static JSONObject toJSONObject(String source) throws JSONException {
        return toJSONObject(new JSONTokener(source));
    }

    private static JSONObject toJSONObject(JSONTokener x) throws JSONException {
        JSONObject object = new JSONObject();
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        for (;;) {
            c = x.nextClean();
            switch (c) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return object;
                default:
                    x.back();
                    key = x.nextValue().toString();
            }

            /*
             * The key is followed by ':'. We will also tolerate '=' or '=>'.
             */

            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            object.put(key, x.nextValue());

            /*
             * Pairs are separated by ','. We will also tolerate ';'.
             */

            switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == '}') {
                        return object;
                    }
                    x.back();
                    break;
                case '}':
                    return object;
                default:
                    throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public static void main(String[] args) {
        Response res = new Response();
        res.addError("doAction", "您的余额已不足,请重新选择时间或充值");
        res.addError("doAction", "您的余额已不足,请重新选择时间或充值");
        String json = res.toJSON();
        System.out.println("JSON=" + json);
        Response res1 = Response.valueOf(json);
        res1.addMessage("test", "测试增加后的内容");
        System.out.println("Resonse=" + res1);
        System.out.println("getStringMessage=" + res1.getStringMessage("test"));
        String responseStr = "{\"code\":500,\"error\":true,\"messages\":{\"system\":\"上传失败:no protocol: www.pconline.com.cn\"},\"ok\":false,\"redirect\":false,\"verify\":false,\"version\":2}";
        Response response = Response.valueOf(responseStr);
        System.out.println(response.getCode());
        System.out.println(response.isError());
        System.out.println(response.isOk());
        System.out.println(response.isVerify());
        System.out.println(response.getStringMessage("system"));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setMessages(Map<String, Object> messages) {
        this.messages = messages;
    }
}
