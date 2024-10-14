package com.muluofeng.easycode.core.dao;

import com.muluofeng.easycode.core.dto.SearchTableDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 */
@Mapper
public interface SysGeneratorDao {

	List<Map<String, Object>> queryList(@Param("req") SearchTableDTO req);

	int queryTotal(@Param("req") SearchTableDTO req);

	Map<String, String> queryTable(String tableName);

	List<Map<String, String>> queryColumns(String tableName);
}
