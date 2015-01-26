package com.ozstrategy.webapp.command.appstore;

import com.ozstrategy.model.appstore.AppStore;
import com.ozstrategy.webapp.command.BaseEntityCommand;

/**
 * Created by lihao on 1/6/15.
 */
public class AppStoreCommand extends BaseEntityCommand {
    private Long id;
    private String url;
    private String version;
    private String description;
    private String platform;
    public AppStoreCommand(AppStore appStore) {
//        super(appStore);
        this.id= appStore.getId();
        this.url= appStore.getUrl();
        this.version= appStore.getVersion();
        this.description= appStore.getDescription();
        this.platform=appStore.getPlatform()!=null?appStore.getPlatform().name():null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
