package com.vasu.manzana.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vasu.manzana.model.Query;
import com.vasu.manzana.model.QueryNickName;

public interface QueryNickNameRepository extends CrudRepository<QueryNickName, Serializable >{

	List<Query> findByQueryId(Integer queryId);
	
}
