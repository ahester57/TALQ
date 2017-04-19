package edu.umsl.superclickers.app;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ModelHolder {

    public static final String USER_MODEL = "UserModel";
    public static final String QUIZ_MODEL = "QuizModel";
    private Map<String, WeakReference<Object>> modelData = new HashMap<>();

    private static final ModelHolder holder = new ModelHolder();

    public static ModelHolder getInstance() {
        return holder;
    }

    public void saveModel(String key, Object model) {
        modelData.put(key, new WeakReference<>(model));
    }

    public Object getModel(String key) {
        WeakReference<Object> weakObj = modelData.get(key);
        if (weakObj != null) {
            return weakObj.get();
        }
        return null;
    }
}
