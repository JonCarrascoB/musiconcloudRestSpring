package com.ipartek.formacion.rest.musiconcloud.controller;

import java.util.ArrayList;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.formacion.rest.musiconcloud.domain.Categoria;
import com.ipartek.formacion.rest.musiconcloud.domain.ReponseMensaje;
import com.ipartek.formacion.rest.musiconcloud.model.CategoriaRepository;


@RestController
@RequestMapping(value= "/categoria/")
public class CategoriasController {
	
	@Autowired 								 // nuestro antiguo .getInstance
	CategoriaRepository categoriaRepository; //nuestro antiguo DAO
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> listar(){
		
		ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.I_AM_A_TEAPOT);
		
		try {
			
			ArrayList<Categoria> lista = (ArrayList<Categoria>) categoriaRepository.findAll();
			if (!lista.isEmpty()) {
				response = new ResponseEntity<Object>(lista, HttpStatus.OK);
			} else {
				response = new ResponseEntity<Object>(lista, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> detalle(@PathVariable int id) {
		ResponseEntity<Object> response = null;
		try {
			Optional<Categoria> categoria = categoriaRepository.findById(id);
			if (!categoria.isPresent()) {
				response = new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
			} else {
				response = new ResponseEntity<Object>(categoria, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> eliminar(@PathVariable int id) {

		ResponseEntity<Object> response = null;
		try {

			categoriaRepository.deleteById(id);
			response = new ResponseEntity<Object>(new ReponseMensaje("Categoria eliminada") , HttpStatus.OK);

		} catch (EmptyResultDataAccessException e) {
			response = new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> insertar(@RequestBody Categoria categoria) {

		ResponseEntity<Object> response = null;
		try {
			categoriaRepository.save(categoria);
			response = new ResponseEntity<Object>(categoria, HttpStatus.CREATED);

		} catch (DataIntegrityViolationException e) {

			response = new ResponseEntity<Object>(new ReponseMensaje("La categoria insertada ya existe"),
					HttpStatus.CONFLICT);
		} catch (ConstraintViolationException e) {
			response = new ResponseEntity<Object>(new ReponseMensaje("El nombre de la categoria no puede estar en blanco, y debe tener entre 3 y 50 caracteres"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;

	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> update(@RequestBody Categoria categoria, @PathVariable int id) {

		ResponseEntity<Object> response = null;
		try {
			categoria.setId(id);
			categoriaRepository.save(categoria);
			response = new ResponseEntity<Object>(categoria, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;

	}

}
