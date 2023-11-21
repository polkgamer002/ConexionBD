/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.conexionbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ConexionBD {

    static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
    static final String USER = "dam2";
    static final String PASS = "1234";
    static String QUERY = "";

    public static void main(String[] args) {

        boolean existe = buscaNombre();
        System.out.println("¿Existe el juegon en la tabla?: " + existe);
        lanzaConsulta();
    }

    public static boolean buscaNombre() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del videojuego a buscar:");
        String nombre = scanner.nextLine();

        String query = "SELECT COUNT(*) FROM videojuegos WHERE Nombre = '" + nombre + "'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        return false;
    }

       public static void lanzaConsulta() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce tu consulta SQL:");
        String consultaSQL = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    System.out.print(value + " ");
                }
                System.out.println();
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void nuevoRegistro(String nombre, String genero, String fechaLanzamiento, String compañia, float precio) {
        String query = "INSERT INTO videojuegos (Nombre, Género, FechaLanzamiento, Compañía, Precio) VALUES ('" 
                        + nombre + "', '" + genero + "', '" + fechaLanzamiento + "', '" + compañia + "', " + precio + ")";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            int affectedRows = stmt.executeUpdate(query);
            if (affectedRows > 0) {
                System.out.println("Un nuevo videojuego ha sido añadido con éxito.");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método que solicita los datos al usuario
    public static void nuevoRegistro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del videojuego:");
        String nombre = scanner.nextLine();

        System.out.println("Introduce el género del videojuego:");
        String genero = scanner.nextLine();

        System.out.println("Introduce la fecha de lanzamiento (YYYY-MM-DD):");
        String fechaLanzamiento = scanner.nextLine();

        System.out.println("Introduce la compañía del videojuego:");
        String compañia = scanner.nextLine();

        System.out.println("Introduce el precio del videojuego:");
        float precio = scanner.nextFloat();

        nuevoRegistro(nombre, genero, fechaLanzamiento, compañia, precio);
        scanner.close();
    }

 public static void eliminarRegistro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del videojuego a eliminar:");
        String nombre = scanner.nextLine();

        String query = "DELETE FROM videojuegos WHERE Nombre = '" + nombre + "'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            int affectedRows = stmt.executeUpdate(query);
            if (affectedRows > 0) {
                System.out.println("El videojuego ha sido eliminado con éxito.");
            } else {
                System.out.println("No se encontró el videojuego con ese nombre.");
            }
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
}
