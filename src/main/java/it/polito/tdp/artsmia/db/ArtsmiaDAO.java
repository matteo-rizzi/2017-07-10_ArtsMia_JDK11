package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public void listObjects(Map<Integer, ArtObject> idMap) {

		String sql = "SELECT * from objects";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if (!idMap.containsKey(res.getInt("object_id"))) {

					ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"),
							res.getString("continent"), res.getString("country"), res.getInt("curator_approved"),
							res.getString("dated"), res.getString("department"), res.getString("medium"),
							res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"),
							res.getString("rights_type"), res.getString("role"), res.getString("room"),
							res.getString("style"), res.getString("title"));
					idMap.put(res.getInt("object_id"), artObj);
				}
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer, ArtObject> idMap) {
		String sql = "SELECT eo1.object_id AS primo, eo2.object_id AS secondo, COUNT(eo1.exhibition_id) AS peso FROM exhibition_objects AS eo1, exhibition_objects AS eo2 WHERE eo1.exhibition_id=eo2.exhibition_id AND eo1.object_id < eo2.object_id GROUP BY eo1.object_id, eo2.object_id";
	
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> list = new ArrayList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Adiacenza a = new Adiacenza(idMap.get(res.getInt("primo")), idMap.get(res.getInt("secondo")), res.getInt("peso"));
				list.add(a);
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}

}
