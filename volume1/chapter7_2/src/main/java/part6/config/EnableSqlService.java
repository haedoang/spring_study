package part6.config;

import org.springframework.context.annotation.Import;

/**
 * author : haedoang
 * date : 2022/03/25
 * description :
 */
@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {
}
