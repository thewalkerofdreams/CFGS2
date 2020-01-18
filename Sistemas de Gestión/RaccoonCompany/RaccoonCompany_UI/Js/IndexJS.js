$(document).ready(function () {
    // Activate tooltip
    $('[data-toggle="tooltip"]').tooltip();

    // Select/Deselect checkboxes
    var checkbox = $('table tbody input[type="checkbox"]');
    $("#selectAll").click(function () {
        if (this.checked) {
            checkbox.each(function () {
                this.checked = true;
            });
        } else {
            checkbox.each(function () {
                this.checked = false;
            });
        }
    });
    checkbox.click(function () {
        if (!this.checked) {
            $("#selectAll").prop("checked", false);
        }
    });
});

window.onload = inicializa;

function inicializa() {
    cargarListadoPersonasConDepartamento();
    document.getElementById("btnAddPerson").addEventListener("click", insertarPersona, false);
}

function cargarListadoPersonasConDepartamento() {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/departamentosapi");

    //Definicion estados
    miLlamada.onreadystatechange = function () {

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            var arrayDepartamentos = JSON.parse(miLlamada.responseText);//Obtenemos la lista de departamentos
            cargarListadoPersonas(arrayDepartamentos);//Cargamos la lista de personas
        }
    };

    miLlamada.send();
}

function cargarListadoPersonas(arrayDepartamentos) {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/personasapi");

    //Definicion estados
    miLlamada.onreadystatechange = function () {

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            var table = document.getElementById("tBodyEmployee");
            var arrayPersonas = JSON.parse(miLlamada.responseText);

            for (i = 0; i < arrayPersonas.length; i++) {
                var tr = document.createElement('tr');
                document.getElementById("tBodyEmployee").appendChild(tr);

                var td = document.createElement('td');//Creamos un tag <td> para el nombre del empleado
                td.innerHTML = "" + arrayPersonas[i].nombre + "";
                tr.appendChild(td);

                var td2 = document.createElement('td');//Creamos un tag <td> para el apellido del empleado
                td2.innerHTML = "" + arrayPersonas[i].apellidos + "";
                tr.appendChild(td2);

                var td3 = document.createElement('td');//Creamos un tag <td> para la fecha de nacimiento del empleado
                td3.innerHTML = "" + arrayPersonas[i].fechaNacimiento + "";
                tr.appendChild(td3);

                var td4 = document.createElement('td');//Creamos un tag <td> para el departamento del empleado
                var nombreEncontrado = false;
                for (j = 0; j < arrayDepartamentos.length && !nombreEncontrado; j++) {
                    if (arrayDepartamentos[j].id == arrayPersonas[i].idDepartamento) {
                        td4.innerHTML = "" + arrayDepartamentos[j].nombre + "";
                        nombreEncontrado = true;
                    }
                }
                tr.appendChild(td4);

                var tdButtons = document.createElement("td");//Agregamos un tag <td> para los botones

                var edit = document.createElement("input");
                edit.setAttribute("type", "image");
                edit.setAttribute("id", arrayPersonas[i].idPersona);
                edit.setAttribute("src", "../Resources/Images/icon_edit.png");
                edit.setAttribute("width", "30");
                edit.setAttribute("heigth", "30");
                edit.addEventListener("click", clickEditar, false);

                var remove = document.createElement("input");
                remove.setAttribute("type", "image");
                remove.setAttribute("id", arrayPersonas[i].idPersona);
                remove.setAttribute("src", "../Resources/Images/icon_delete.png");
                remove.setAttribute("width", "30");
                remove.setAttribute("heigth", "30");
                remove.addEventListener("click", clickEliminar, false);

                tdButtons.appendChild(edit);//Agregamos los botones al tag <td>
                tdButtons.appendChild(remove);

                tr.appendChild(tdButtons);//Le asignamos el tag <td> al tag <tr> 

                table.appendChild(tr);//Le asignamos el tag <tr> a la tabla
            }
        }
    };

    miLlamada.send();
}

function reloadTable() {//Actualizamos la tabla
    var table = document.getElementById("tableEmployee");
    var rowCount = table.rows.length;//Obtenemos el número de filas de la tabla
    for (var i = 1; i < rowCount; i++) {//Eliminamos todas las filas de la tabla
        table.deleteRow(1);
    }
    cargarListadoPersonasConDepartamento();//Volvemos a cargar el listado de personas
}

//Hace una peticion get de una persona segun id (GET{ID})
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

function clickEditar() {//Lo utilizaremos para actualizar un empleado

    var edit = document.getElementById("editEmployeeModal");
    var save = document.getElementById("inputSaveEmployee");
    var cancel = document.getElementById("inputCancelSaveEmployee");
    edit.style.display = "block";

    var pers = consultarPersona(this.id);
    var id = this.id;

    //var id = document.getElementById("idPersona");
    //id.setAttribute("value", pers.idPersona);
    document.getElementById("nombreEdit").setAttribute("value", pers.nombre);
    document.getElementById("apellidosEdit").setAttribute("value", pers.apellidos);
    //document.getElementById("telefonoE").setAttribute("value", pers.telefono);
    var porque2 = pers.fechaNacimiento.substr(0, 4) + "-" + pers.fechaNacimiento.substr(5, 2) + "-" + pers.fechaNacimiento.substr(8, 2);
    document.getElementById("fechaNacimientoEdit").setAttribute("value", porque2);
    document.getElementById("departamentoEdit").setAttribute("value", pers.idDepartamento);

    save.onclick = function () {
        var empleado = new Object();
        empleado.idPersona = id;
        empleado.nombre = document.getElementById("nombreEdit").value;
        empleado.apellidos = document.getElementById("apellidosEdit").value;
        empleado.telefono = "000000000";
        empleado.fechaNacimiento = document.getElementById("fechaNacimientoEdit").value;
        empleado.idDepartamento = document.getElementById("departamentoEdit").value;

        var miLlamada = new XMLHttpRequest();
        miLlamada.open("PUT", "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/" + id);
        miLlamada.setRequestHeader('Content-type', 'application/json');

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            //actualizar
            alert("Employee edited!");
            edit.style.display = "none";
            reloadTable();
        }

        miLlamada.send(JSON.stringify(empleado));
    }

    cancel.onclick = function () {
        edit.style.display = "none";
    }
}

//Eliminar una persona (DELETE)
function clickEliminar() {

    //modal
    var id = this.id;
    var modal = document.getElementById("deleteEmployeeModal");
    var acept = document.getElementById("inputAceptDelete");
    var cancel = document.getElementById("inputCancelDelete");
    modal.style.display = "block";

    //Si acepta
    acept.onclick = function () {
        modal.style.display = "none";

        //eliminar
        var llamadaEliminar = new XMLHttpRequest();
        llamadaEliminar.open("DELETE", "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/" + id);
        llamadaEliminar.onreadystatechange = function () {

            if (llamadaEliminar.readyState == 4 && llamadaEliminar.status == 204) {

                reloadTable();//Actualizamos la tabla
                alert("Employee deleted!");
            }
        }
        llamadaEliminar.send();
    }

    //Si cancela
    cancel.onclick = function () {
        modal.style.display = "none";
    }
}

function insertarPersona() {

    var createModal = document.getElementById("addEmployeeModal");
    createModal.style.display = "block";

    var id = 1;
    var nombre = document.getElementById("nombre").value;
    var apellidos = document.getElementById("apellidos").value;
    var fechaNacimiento = document.getElementById("fechaNacimiento").value;
    //var telefono = document.getElementById("telefono").value;
    var idDepartamento = document.getElementById("departamento").value;

    var empleado = new Object();
    empleado.idPersona = id;
    empleado.nombre = nombre;
    empleado.apellidos = apellidos;
    empleado.fechaNacimiento = fechaNacimiento;
    empleado.telefono = "000000000";
    empleado.idDepartamento = idDepartamento;

    insertarPersonaPost(empleado);
  
    function insertarPersonaPost(empleado) {//Insertamos el empleado por post

        var llamadaInsertar = new XMLHttpRequest();
        llamadaInsertar.open('POST', "https://crudpersonasui-victor.azurewebsites.net/api/PersonasAPI/", false);
        llamadaInsertar.setRequestHeader('Content-type', 'application/json');

        llamadaInsertar.onreadystatechange = function () {
            if (llamadaInsertar.readyState == 4 && llamadaInsertar.status == 204) {
                //createModal.style.display = "none";
                alert("Employee inserted!");
                reloadTable();
            }
        }

        llamadaInsertar.send(JSON.stringify(empleado));
    }

    
}