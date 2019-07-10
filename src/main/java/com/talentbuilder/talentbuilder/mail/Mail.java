package com.talentbuilder.talentbuilder.mail;

import java.util.List;
import java.util.Map;

public class Mail {

    private String from;
    private String to;
    private String subject;
    private List<Object> attachments;
    private Map<String, Object> model;
    private String template;
    private String[] toMany;
    private boolean isMany;

    public Mail() {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String[] getToMany() {
		return toMany;
	}

	public void setToMany(String[] toMany) {
		this.toMany = toMany;
	}

	public boolean isMany() {
		return isMany;
	}

	public void setMany(boolean isMany) {
		this.isMany = isMany;
	}
    
    
}
