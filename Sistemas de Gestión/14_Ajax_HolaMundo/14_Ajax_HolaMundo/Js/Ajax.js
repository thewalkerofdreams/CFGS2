window.onload = inicializa;

function inicializa() {
    document.getElementById("btnSaludar").addEventListener("click", pedirSaludo);
    document.getElementById("btnPedirApellido").addEventListener("click", pedirApellido);
}

function pedirSaludo() {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "../Pages/Hola.html");

    //Definicion estados
    miLlamada.onreadystatechange = function () {

        
        if (miLlamada.readyState < 4) {
            //alert(miLlamada.readyState);
            //aquí se puede poner una imagen de un reloj o un texto “Cargando”
            document.getElementById("divSaludo").innerHTML = "Cargando...";
            
        }
        else
            if (miLlamada.readyState == 4 && miLlamada.status == 200) {

                //alert(miLlamada.status);

                var mensaje = miLlamada.responseText;
                document.getElementById("divSaludo").innerHTML = mensaje;

            }

    };

    miLlamada.send();
}

function pedirApellido() {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/personasapi");

    //Definicion estados
    miLlamada.onreadystatechange = function () {


        if (miLlamada.readyState < 4) {
            //alert(miLlamada.readyState);
            //aquí se puede poner una imagen de un reloj o un texto “Cargando”
            document.getElementById("divApellidos").innerHTML = "Cargando...";

        }
        else
            if (miLlamada.readyState == 4 && miLlamada.status == 200) {

                //alert(miLlamada.status);

                var arrayPersonas = JSON.parse(miLlamada.responseText);
                var persona = arrayPersonas[0]; 
                document.getElementById("divApellidos").innerHTML = persona.apellidos;

            }

    };

    miLlamada.send();
}
