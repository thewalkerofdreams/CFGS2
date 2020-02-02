window.onload = inicializa;

function inicializa() {
    cargarListadoPersonasConDepartamento();
    document.getElementById("btnInsertar").addEventListener("click", insertPersona, false)
}
function cargarListadoPersonasConDepartamento() {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/departamentosapi");

   //Obtenemos un listado de departamentos
    miLlamada.onreadystatechange = function () {

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            var listaDep = JSON.parse(miLlamada.responseText);
            crearTabla(listaDep);//Creamos la tabla
        }
    };

    miLlamada.send();
}

//Crea una tabla en la que se muestran personas con sus departamentos.
function crearTabla(listaDep) {
    //Obtenemos un listado de personas.
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/personasapi");

    miLlamada.onreadystatechange = function () {

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            var listadoPersonas = JSON.parse(miLlamada.responseText);
            //Crear tabla
            var table = document.getElementById("tablaPersonas");

            //insertar filas
            for (i = 0; i < listadoPersonas.length; i++) {
                var tr = document.createElement('tr');
                document.getElementById("tablaPersonas").appendChild(tr);

                //Creamos un tag <td> para cada propiedad de la persona.
                var td = document.createElement('td');
                td.innerHTML = "" + listadoPersonas[i].nombre + "";
                tr.appendChild(td);

                var td2 = document.createElement('td');
                td2.innerHTML = "" + listadoPersonas[i].apellidos + "";
                tr.appendChild(td2);

                var td3 = document.createElement('td');
                td3.innerHTML = "" + listadoPersonas[i].fechaNacimiento + "";
                tr.appendChild(td3);

                var td4 = document.createElement('td');
                td4.innerHTML = "" + listadoPersonas[i].telefono + "";
                tr.appendChild(td4);

                var td5 = document.createElement('td');
                td5.innerHTML = "" + listaDep[listadoPersonas[i].idDepartamento-1].nombre + "";
                tr.appendChild(td5);

                var tdButtons = document.createElement("td");//Agregamos un tag <td> para los botones
                //Estilo del boton de editar
                var edit = document.createElement("input");
                edit.setAttribute("type", "image");
                edit.setAttribute("id", listadoPersonas[i].idPersona); //El ID sera el mismo que el de la persona
                edit.setAttribute("src", "../Recursos/Imagenes/edit.jpg");
                edit.setAttribute("width", "30");
                edit.setAttribute("heigth", "30");
                edit.addEventListener("click", clickEditar, false);
                //Estilo del boton eliminar.
                var remove = document.createElement("input");
                remove.setAttribute("type", "image");
                remove.setAttribute("id", listadoPersonas[i].idPersona); //El ID sera el mismo que el de la persona, mas tarde lo usare.
                remove.setAttribute("src", "../Recursos/Imagenes/delete.png");
                remove.setAttribute("width", "30");
                remove.setAttribute("heigth", "30");
                remove.addEventListener("click", clickEliminar, false); //Añadimos el EventListener
                //Agregamos los botones y la fila a la tabla.
                tdButtons.appendChild(edit);
                tdButtons.appendChild(remove);
                tr.appendChild(tdButtons);

                table.appendChild(tr);
            }
        }
    };

    miLlamada.send();
}

//Rellena el modal para crear persona y ejecuta la funcion de insercion
function insertPersona() {

    var crear = document.getElementById("modalCrear");
    var btnCrear = document.getElementById("botonCrear");
    var btnCancelar = document.getElementById("botonCancelar");
    crear.style.display = "block";

    btnCrear.onclick = function () {

        var oP = new Object();
        oP.idPersona = 1; //Da igual que numero le demos, al insertarse se le asigna el correcto.
        oP.nombre = document.getElementById("nombre").value;
        oP.apellidos = document.getElementById("apellidos").value;
        oP.fechaNacimiento = document.getElementById("fechaNacimiento").value;
        oP.telefono = document.getElementById("telefono").value;
        oP.idDepartamento = document.getElementById("idDepart").value;

        insertarPersona(oP);
    }

    btnCancelar.onclick = function () {
        crear.style.display = "none";
    }

}

//Inserta una persona (POST)
function insertarPersona(pers) {

    var llamadaInsertar = new XMLHttpRequest();
    llamadaInsertar.open('POST', "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/", false);
    llamadaInsertar.setRequestHeader('Content-type', 'application/json');

    llamadaInsertar.onreadystatechange = function () {
        if (llamadaInsertar.readyState == 4 && llamadaInsertar.status == 200) {
            alert("Persona insertada con exito.");
            crear.style.display = "none";
            actualizarTabla();
        }
    }

    llamadaInsertar.send(JSON.stringify(pers));
}

//Actualizar tabla
function actualizarTabla() {
    var table = document.getElementById("tablaPersonas");
    var rowCount = table.rows.length;
    for (var i = 1; i < rowCount; i++) {
        table.deleteRow(1);
    }
    cargarListadoPersonasConDepartamento();
}


//Eliminar una persona (DELETE)
function clickEliminar() {

    //modal
    var id = this.id; //Este es el ID del boton que coincide con el de la persona.
    var modal = document.getElementById("modalEliminar");
    var aceptar = document.getElementById("AceptarEliminar");
    var cancelar = document.getElementById("CancelarEliminar");
    modal.style.display = "block";

    //Si acepta
    aceptar.onclick = function () {
        modal.style.display = "none";

        //eliminar
        var llamadaEliminar = new XMLHttpRequest();
        llamadaEliminar.open("DELETE", "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/"+id);
        llamadaEliminar.onreadystatechange = function () {
            if (llamadaEliminar.readyState == 4 && llamadaEliminar.status == 204) { //Tiene que ser 204 (no content) porque no devuelve nada.
                //actualizar
                alert("Persona borrada con exito.");
                actualizarTabla();
                
            }
        }
        llamadaEliminar.send();
    }

    //Si cancela
    cancelar.onclick = function () {
        modal.style.display = "none";
    }
}


//Actualiza una persona (PUT)
function clickEditar() {

    var editar = document.getElementById("modalEditar");
    var btnConfirmar = document.getElementById("botonConfirmar");
    var btnCancelar = document.getElementById("botonCancelarEditar");
    editar.style.display = "block";

    var pers = consultarPersona(this.id);

    //Establecemos los campos con los datos de la persona seleccionada.
    var id = document.getElementById("idPersona");
    id.setAttribute("value", pers.idPersona);
    document.getElementById("nombreE").setAttribute("value", pers.nombre);
    document.getElementById("apellidosE").setAttribute("value", pers.apellidos);
    document.getElementById("telefonoE").setAttribute("value", pers.telefono);
    var formato = pers.fechaNacimiento.substr(0, 4) + "-" + pers.fechaNacimiento.substr(5, 2) + "-" + pers.fechaNacimiento.substr(8, 2);
    document.getElementById("fechaNacimientoE").setAttribute("value", formato);
    document.getElementById("idDepartE").value = pers.idDepartamento;

    btnConfirmar.onclick = function () {
        id.focus();
        var per = new Object();
        per = recogerDatosPersonaModalEditar();
        var miLlamada = new XMLHttpRequest();
        miLlamada.open("PUT", "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/" + per.idPersona, false); //Le hacia falta el false, dios sabra porque.
        miLlamada.setRequestHeader('Content-type', 'application/json');

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            //actualizar
            alert("Persona editada con exito!");
            editar.style.display = "none";
            actualizarTabla();
        }

        miLlamada.send(JSON.stringify(per));
    }

    btnCancelar.onclick = function () {
        editar.style.display = "none";
    }
}

//Hace una peticion get de una persona segun id y la devuelve.
function consultarPersona(id) {

    var persona;
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/" + id, false);

    miLlamada.onreadystatechange = function () {
        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            persona = JSON.parse(miLlamada.responseText);
        }
    }

    miLlamada.send();

    return persona;
}

//El nombre es bastante autodocumentado
function recogerDatosPersonaModalEditar() {

    var persona = new Object();
    persona.idPersona = document.getElementById("idPersona").value;
    persona.nombre = document.getElementById("nombreE").value;
    persona.apellidos = document.getElementById("apellidosE").value;
    persona.telefono = document.getElementById("telefonoE").value;
    persona.fechaNacimiento = document.getElementById("fechaNacimientoE").value;
    persona.idDepartamento = document.getElementById("idDepartE").value;

    return persona;

}