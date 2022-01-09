package misaka.app.push;

import artoria.data.AbstractExtraData;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class PushMessage extends AbstractExtraData implements Serializable {
    private String id;
    private String appId;
    private String notificationTitle;
    private String notificationContent;
    private String messageTitle;
    private String messageContent;
    private Map<String, String> messageExtras;
    private Map<String, String> notificationExtras;
    private Collection<String> tagsAnd;
    private Collection<String> tagsOr;
    private Collection<String> tagsNot;
    private Collection<String> aliases;
    private Collection<String> registrationIds;
    private Collection<String> platforms;
    private Boolean allAudience;
    private Boolean allPlatform;
    private Boolean production;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Map<String, String> getMessageExtras() {
        return messageExtras;
    }

    public void setMessageExtras(Map<String, String> messageExtras) {
        this.messageExtras = messageExtras;
    }

    public Map<String, String> getNotificationExtras() {
        return notificationExtras;
    }

    public void setNotificationExtras(Map<String, String> notificationExtras) {
        this.notificationExtras = notificationExtras;
    }

    public Collection<String> getTagsAnd() {
        return tagsAnd;
    }

    public void setTagsAnd(Collection<String> tagsAnd) {
        this.tagsAnd = tagsAnd;
    }

    public Collection<String> getTagsOr() {
        return tagsOr;
    }

    public void setTagsOr(Collection<String> tagsOr) {
        this.tagsOr = tagsOr;
    }

    public Collection<String> getTagsNot() {
        return tagsNot;
    }

    public void setTagsNot(Collection<String> tagsNot) {
        this.tagsNot = tagsNot;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    public void setAliases(Collection<String> aliases) {
        this.aliases = aliases;
    }

    public Collection<String> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(Collection<String> registrationIds) {
        this.registrationIds = registrationIds;
    }

    public Collection<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Collection<String> platforms) {
        this.platforms = platforms;
    }

    public Boolean getAllAudience() {
        return allAudience;
    }

    public void setAllAudience(Boolean allAudience) {
        this.allAudience = allAudience;
    }

    public Boolean getAllPlatform() {
        return allPlatform;
    }

    public void setAllPlatform(Boolean allPlatform) {
        this.allPlatform = allPlatform;
    }

    public Boolean getProduction() {
        return production;
    }

    public void setProduction(Boolean production) {
        this.production = production;
    }
}
