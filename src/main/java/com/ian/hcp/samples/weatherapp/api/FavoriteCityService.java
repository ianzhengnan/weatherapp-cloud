package com.ian.hcp.samples.weatherapp.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.ian.hcp.samples.weatherapp.model.FavoriteCity;

@Path("/cities")
@Produces({MediaType.APPLICATION_JSON})
public class FavoriteCityService {

	@SuppressWarnings("unchecked")
	@GET
	@Path("/")
	public List<FavoriteCity> getFavoriteCities(@Context SecurityContext ctx){
		
		List<FavoriteCity> retVal = null;
			
		Map<String, String> props = new HashMap<String, String>();
		
		props = this.getTenant(ctx);
		
		EntityManager em = this.getEntityManagerFactory().createEntityManager(props);
		
		retVal = em.createNamedQuery("FavoriteCities").getResultList();
		
		return retVal;
	}
	
	@GET
	@Path("/{id}")
	public FavoriteCity getFavoriteCity(@PathParam(value = "id") String id, @Context SecurityContext ctx){
		
		FavoriteCity retVal = null;
		
		Map<String, String> props = new HashMap<String, String>();
		
		props = this.getTenant(ctx);
		
		EntityManager em = this.getEntityManagerFactory().createEntityManager(props);
		
		try{
			Query query = em.createNamedQuery("FavoriteCityById");
			query.setParameter("id", id);
			retVal = (FavoriteCity) query.getSingleResult();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			em.close();
		}
		
		return retVal;
	}
	
	@GET
	@Path("/{id}/weather")
	@Produces({MediaType.APPLICATION_JSON})
	public String getWeatherInformation(@PathParam(value = "id") String id){
		WeatherService weatherService = new WeatherService();
		return weatherService.getWeatherInformation(id, null);
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/")
	public List<FavoriteCity> addFavoriteCity(FavoriteCity city, @Context SecurityContext ctx){
		
		List<FavoriteCity> retVal = null;
		
		Map<String, String> props = new HashMap<String, String>();
		
		props = this.getTenant(ctx);
		
		EntityManager em = this.getEntityManagerFactory().createEntityManager(props);
		
		try{
			em.getTransaction().begin();
			em.persist(city);
			em.getTransaction().commit();
			
			retVal = em.createNamedQuery("FavoriteCities").getResultList();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			em.close();
		}
		
		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/{id}")
	public List<FavoriteCity> removeFavoriteCity(@PathParam(value = "id") String id, @Context SecurityContext ctx){
		
		List<FavoriteCity> retVal = null;
		
		Map<String, String> props = new HashMap<String, String>();
		
		props = this.getTenant(ctx);
		
		EntityManager em = this.getEntityManagerFactory().createEntityManager(props);
		
		try{
			Query query = em.createNamedQuery("FavoriteCityById");
			query.setParameter("id", id);
			FavoriteCity city = (FavoriteCity)query.getSingleResult();
			
			if(city != null){
				em.getTransaction().begin();
				em.remove(city);
				em.getTransaction().commit();
			}
			retVal = em.createNamedQuery("FavoriteCities").getResultList();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			em.close();
		}
		
		return retVal;
	}
	
	protected DataSource getDataSource() {
		
		DataSource retVal = null;
		
		try{
			InitialContext ctx = new InitialContext();
			retVal = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
		}catch(NamingException ex){
			ex.printStackTrace();
		}
		return retVal;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected EntityManagerFactory getEntityManagerFactory(){
		EntityManagerFactory retVal = null;
		
		try{
			Map properties = new HashMap();
			DataSource ds = this.getDataSource();
			properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
			retVal = Persistence.createEntityManagerFactory("application", properties);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return retVal;
	}
	
	private Map<String, String> getTenant(@Context SecurityContext ctx){
		
		String userName = (ctx.getUserPrincipal() != null)? 
				ctx.getUserPrincipal().getName() : "anonymous";
				
		Map<String, String> props = new HashMap<String, String>();
		
		props.put("tenant.id", userName);
		
		return props;
		
	}
}
