package com.vasu.manzana.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.vasu.manzana.model.Query;

public interface QueryRepository extends CrudRepository<Query, Serializable >{

	Query findByName(String name);
	Query findById(Integer id);
	
}
