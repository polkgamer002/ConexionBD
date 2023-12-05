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

    //declaramos la url, el usuario, la contraseña para conectarnos a la base de datos
    static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
    static final String USER = "dam2";
    static final String PASS = "1234";
    //declaramos la query para que la ejecute en la base de datos,
    //pero vacia ya que luego la haremos
    static String QUERY = "";

    public static void main(String[] args) {
        //Llamamos al método buscanombre para buscar un videojuego y 
        //almacena el resultado en existe
        boolean existe = buscaNombre();
        //mostramos si el videojuego existe en la tabla
        System.out.println("¿Existe el juegon en la tabla?: " + existe);
        //Llamamos al método lanzaconsulta para que el usuario ejecute consultas SQL
        lanzaConsulta();
    }

    public static boolean buscaNombre() {
        //creamos el metodo scanner para que el usuario introduzca los datos que quiera buscar
        Scanner scanner = new Scanner(System.in);
        //le pide al usuario que introduzca el nombre del videojuego a buscar
        System.out.println("Introduce el nombre del videojuego a buscar:");
        String nombre = scanner.nextLine();

        //construimos una consulta SQL para contar cuántos registros 
        //coinciden con el nombre que el usuario ha escrito
        String query = "SELECT COUNT(*) FROM videojuegos WHERE Nombre = '" + nombre + "'";
        //abrimos una conexion y un statement para conectarnos con la base de datos 
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            //comprobamos si la consulta encontró algún registro
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            //cerramos el statement y la conexion
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        //devolvemos false si no se encontraron registros
        return false;
    }

    public static void lanzaConsulta() {
        //creamos el metodo scanner para que el usuario introduzca los datos que quiera ver
        Scanner scanner = new Scanner(System.in);
        //le pedimos al usuario que introduzca la consulta sql para que elija que haer
        System.out.println("Introduce tu consulta SQL:");
        String consultaSQL = scanner.nextLine();
        //abrimos una conexion y un statement para conectarnos con la base de datos
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            int columnCount = rs.getMetaData().getColumnCount();
            //mientras haya lineas que leer, recorremos las columnas, las guardamos y las mostramos
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    System.out.print(value + " ");
                }
                System.out.println();
            }
            //cerramos el statement y la conexion
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void nuevoRegistro(String nombre, String genero, String fechaLanzamiento, String compañia, float precio) {
        //construimos una consulta SQL para insertar un nuevo registro en la tabla videojuegos
        String query = "INSERT INTO videojuegos (Nombre, Género, FechaLanzamiento, Compañía, Precio) VALUES ('" 
                        + nombre + "', '" + genero + "', '" + fechaLanzamiento + "', '" + compañia + "', " + precio + ")";
        //abrimos una conexion a la base de datos
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            //ejecutamos la consulta SQL para insertar el nuevo videojuego
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

    //creamos un  método que le pide al usuario datos para un nuevo registro
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

        //Llamamos al método para agregar un nuevo registro con los datos proporcionados
        nuevoRegistro(nombre, genero, fechaLanzamiento, compañia, precio);
        scanner.close();
    }

    public static void eliminarRegistro() {
        //creamos el metodo scanner para que el usuario introduzca los datos que quiera eliminar
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del videojuego a eliminar:");
        String nombre = scanner.nextLine();

        //construimos una consulta SQL para eliminar un videojuego sabiendo su nombre
        String query = "DELETE FROM videojuegos WHERE Nombre = '" + nombre + "'";
        //abrimos una conexion y un statement para la base de datos
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            //ejecutamos la consulta SQL para eliminar el videojuego
            int affectedRows = stmt.executeUpdate(query);
            //si existe el videojuego y lo eliminamos, mandamos mensjae de exito
            if (affectedRows > 0) {
                System.out.println("El videojuego ha sido eliminado con éxito.");
            //si no existe el videojuego o no lo eliminamos, mandamos mensjae de fallo    
            } else {
                System.out.println("No se encontró el videojuego con ese nombre.");
            }
            //cerramos el statement y la conexion
            conn.close();
            stmt.close();
        //capturamos la sql exception y mostramos la lista de errores    
        } catch (SQLException e) {
            e.printStackTrace();
        //cerramos el try-catch y el scanner    
        } finally {
            scanner.close();
        }
    }   
}

