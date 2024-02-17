/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.llm.dto.chat;

import java.io.Serializable;
import java.util.List;

public class ChatResp implements Serializable {
    private String id;
    private String object;
    private String model;
    private Long   created;
    private List<Choice> choices;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getObject() {

        return object;
    }

    public void setObject(String object) {

        this.object = object;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public Long getCreated() {

        return created;
    }

    public void setCreated(Long created) {

        this.created = created;
    }

    public List<Choice> getChoices() {

        return choices;
    }

    public void setChoices(List<Choice> choices) {

        this.choices = choices;
    }

}
