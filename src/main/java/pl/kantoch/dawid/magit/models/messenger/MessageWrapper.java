package pl.kantoch.dawid.magit.models.messenger;

import java.util.List;

public class MessageWrapper
{
    private Long allUnread;
    private List<MessageWrapperEntity> entityList;

    public MessageWrapper() {
    }

    public MessageWrapper(Long allUnread, List<MessageWrapperEntity> entityList) {
        this.allUnread = allUnread;
        this.entityList = entityList;
    }

    public Long getAllUnread() {
        return allUnread;
    }

    public void setAllUnread(Long allUnread) {
        this.allUnread = allUnread;
    }

    public List<MessageWrapperEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<MessageWrapperEntity> entityList) {
        this.entityList = entityList;
    }
}
