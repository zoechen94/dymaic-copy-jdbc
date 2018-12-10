package com.xsjt.util;

import com.xsjt.dynamicDataSource.DynamicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SpringJdbcTemplate extends JdbcTemplate {
    @Override
    public DataSource getDataSource() {
        DynamicDataSource router =  (DynamicDataSource) super.getDataSource();
        DataSource acuallyDataSource = router.getAcuallyDataSource();
        return acuallyDataSource;
    }

    public SpringJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }
}
