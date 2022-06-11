package part5.user.sqlservice;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class SqlAdminService implements AdminEventListener {
    private UpdatableSqlRegistry updatableSqlRegistry;

    public void setUpdatableSqlRegistry(UpdatableSqlRegistry updatableSqlRegistry) {
        this.updatableSqlRegistry = updatableSqlRegistry;
    }

    public void updateEventListener(UpdateEvent event) {
        this.updatableSqlRegistry.updateSql("", "");
    }
}
