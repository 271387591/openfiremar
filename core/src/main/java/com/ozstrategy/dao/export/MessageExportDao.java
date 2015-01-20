package com.ozstrategy.dao.export;

import com.ozstrategy.model.export.MessageExport;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/11/15.
 */
public interface MessageExportDao {
    List<MessageExport> list(Map<String,Object> map,RowBounds rowBounds);
    Integer listCount(Map<String,Object> map);
    void save(MessageExport messageExport);
    MessageExport get(Long id);
}
