/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package practica_1;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        // System.out.println(new App().getGreeting());

        HttpClient client;
        HttpGet request;
        HttpResponse response;

        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Digite la URL: ");

        String myUrl = myObj.nextLine(); // Read user input

        client = HttpClientBuilder.create().build();
        request = new HttpGet(myUrl);
        try {
            // Si la petición es exitosa, la página existe.
            response = client.execute(request);
            // int statusCode = response.getStatusLine().getStatusCode();
            // System.out.println(statusCode);
            System.out.println("La URL es válida.");

            // Contar la cantidad de líneas.
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("\nCantidad de líneas del recurso: " + content.lines().count());
            // System.out.println();

            // Contar la cantidad de p, img y form
            Document document = Jsoup.connect(myUrl).get();
            System.out.println("\nCantidad de tags p = " + document.getElementsByTag("p").size());
            System.out.println("Cantidad de tags form = " + document.getElementsByTag("form").size());
            int cantidadImagenesEnParrafos = 0;
            Elements parrafos = document.getElementsByTag("p");
            for(Element parrafo : parrafos){
                cantidadImagenesEnParrafos += parrafo.getElementsByTag("img").size();
            }
            System.out.println("Cantidad de tags img dentro de lod párrafos = " + cantidadImagenesEnParrafos);

            // Número de forms Get con y Post
            Elements formularioGet = document.select("form[method=GET]");
            int numeroDeFormsGet = formularioGet.size();
            Elements formularioPost = document.select("form[method=POST]");
            int numeroDeFormsPost = formularioPost.size();

            System.out.println("\nNúmero de forms con GET: " + numeroDeFormsGet);
            System.out.println("Número de forms con POST: " + numeroDeFormsPost);

            // Campos input
            System.out.println("\nTipos de los campos input:");
            ArrayList<Element> links = document.select("input");
            for (Element input : links) { 		      
                System.out.println(input.attr("type")); 		
           }

            System.out.println("\nRespuestas peticiones a los forms POST");
            Document postRequest;
            for (Element form: document.select("form[method=POST]")) {
                String urlPeticion = form.absUrl("action");
                // System.out.println("Abs url: "+urlPeticion);
                postRequest= Jsoup.connect(urlPeticion).data("asignatura", "practica1").header("matricula", "20170639").post();
                System.out.println("Respuesta de la petición al form POST "+form.attr("name")+": "+postRequest.body().toString());
                System.out.println("\n\n");
            } 

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("La URL no es válida.");
        }

        myObj.close();
        
    }
}
