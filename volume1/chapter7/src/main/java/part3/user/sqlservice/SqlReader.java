package part3.user.sqlservice;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public interface SqlReader {
    //SQL을 외부에서 가져와 SqlRegistry 에 등록한다
    void read(SqlRegistry registry);
}
