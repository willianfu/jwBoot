package com.jiawei.jwboot.mvc.servlet.model;

import java.util.Map;

/**
 * @author : willian fu
 * @version : 1.0
 */
public class ModelAndView extends View{

    protected Map<String, Object> model;

    public ModelAndView(Map<String, Object> model, String view) {
        this.model = model;
        super.setView(view);
    }

    public ModelAndView() {
    }

    public Map<String, Object> getModel() {
        return this.model;
    }

    public void setModel(Map model) {
        this.model = model;
    }
}
