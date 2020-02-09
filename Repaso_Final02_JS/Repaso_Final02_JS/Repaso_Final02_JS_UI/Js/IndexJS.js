window.onload = inicializa;//Inicializamos los elementos básicos como los botones de la pantalla o la lista de empleados

//Este método nos permite inicializar algunos elementos de la pantalla
function inicializa() {
    cargarListadoPersonasConDepartamento();
    document.getElementById("btnAddPerson").addEventListener("click", addClick, false);
}

//Este método nos permite mostrar por pantalla los empleados con su departamento específico.
function cargarListadoPersonasConDepartamento() {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://localhost:44371/api/DepartamentosApi");

    //Definición del estado
    miLlamada.onreadystatechange = function () {//Cada vez que se actualice el estado se llamará a esta función
        if (miLlamada.readyState == 4 && miLlamada.status == 200) {//Si la llamada del tipo GET ha sido correcta
            var arrayDepartamentos = JSON.parse(miLlamada.responseText);//Obtenemos la lista de departamentos
            cargarListadoPersonas(arrayDepartamentos);//Ahora cargaremos la lista de personas con su departamento
        }
    };

    miLlamada.send();
}

//Método llamado por la función cargarListadoPersonasConDepartamento, nos permitirá mostrar por pantalla los
//empleados con su departamento en específico (Los departamentos se obtienen en la función cargarListadoPersonasConDepartamento)
function cargarListadoPersonas(arrayDepartamentos) {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://localhost:44371/api/PersonasApi");

    //Definición del estado
    miLlamada.onreadystatechange = function () {
        if (miLlamada.readyState == 4 && miLlamada.status == 200) {//Si la llamada por GET es correcta
            var table = document.getElementById("tableEmployee");//Instanciamos el elemento table de la página html
            var arrayPersonas = JSON.parse(miLlamada.responseText);//Obtenemos el array de personas (empleados)

            for (i = 0; i < arrayPersonas.length; i++) {//Por cada empleado
                var tr = document.createElement('tr');//Generemos un tag <tr>

                var td = document.createElement('td');//Creamos un tag <td> para el nombre del empleado
                td.innerHTML = "" + arrayPersonas[i].nombre + "";
                tr.appendChild(td);

                var td2 = document.createElement('td');//Creamos un tag <td> para el apellido del empleado
                td2.innerHTML = "" + arrayPersonas[i].apellidos + "";
                tr.appendChild(td2);

                var tdPhone = document.createElement('td');
                tdPhone.innerHTML = "" + arrayPersonas[i].telefono + "";
                tr.appendChild(tdPhone);

                var td3 = document.createElement('td');//Creamos un tag <td> para la fecha de nacimiento del empleado
                td3.innerHTML = "" + arrayPersonas[i].fechaNacimiento + "";
                tr.appendChild(td3);

                var td4 = document.createElement('td');//Creamos un tag <td> para el departamento del empleado
                var nombreEncontrado = false;
                for (j = 0; j < arrayDepartamentos.length && !nombreEncontrado; j++) {//Obtenemos el nombre del departamento
                    if (arrayDepartamentos[j].Id == arrayPersonas[i].idDepartamento) {
                        td4.innerHTML = "" + arrayDepartamentos[j].Nombre + "";//Le asignamos el nombre del departamento a la variable tag
                        nombreEncontrado = true;
                    }
                }
                tr.appendChild(td4);

                var tdButtons = document.createElement("td");//Agregamos un tag <td> para los botones

                var edit = document.createElement("input");//Configuramos el botón de editar para la fila del empleado
                edit.setAttribute("type", "image");
                edit.setAttribute("id", arrayPersonas[i].id);
                edit.setAttribute("src", "../Resources/Images/icon_edit.png");
                edit.setAttribute("width", "30");
                edit.setAttribute("heigth", "30");
                edit.addEventListener("click", editClick, false);

                var remove = document.createElement("input");//Configuramos el botón de eliminar para la fila del empleado
                remove.setAttribute("type", "image");
                remove.setAttribute("id", arrayPersonas[i].id);
                remove.setAttribute("src", "../Resources/Images/icon_delete.png");
                remove.setAttribute("width", "30");
                remove.setAttribute("heigth", "30");
                remove.addEventListener("click", removeClick, false);

                tdButtons.appendChild(edit);//Agregamos los botones al tag <td>
                tdButtons.appendChild(remove);

                tr.appendChild(tdButtons);//Le asignamos la variable td al tag <tr> 

                table.appendChild(tr);//Le asignamos la variable tr a la tabla
            }
        }
    };

    miLlamada.send();//Realizamos la llamada
}

//Esta función nos va a permitir actualizar la tabla de empleados
function reloadTable() {
    var table = document.getElementById("tableEmployee");//Instanciamos el elemento table de la página en una variable
    var rows = table.rows.length;//Obtenemos el número de filas de la tabla
    for (var i = 1; i < rows; i++) {//Eliminamos todas las filas de la tabla
        table.deleteRow(1);
    }
    cargarListadoPersonasConDepartamento();//Volvemos a cargar el listado de empleados en la tabla
}

//Nos permite obtener todos los datos de un empleado en específico
function getEmployee(id) {
    var empleado;
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://localhost:44371/api/PersonasApi/" + id, false);

    miLlamada.onreadystatechange = function () {
        if (miLlamada.readyState == 4 && miLlamada.status == 200) {
            empleado = JSON.parse(miLlamada.responseText);
        }
    }

    miLlamada.send();

    return empleado;
}

//Esta función se activará cada vez que el usuario desee modificar los datos de un empleado
function editClick() {
    var edit = document.getElementById("editEmployeeModal");//Instanciamos el modal que aparecerá
    var save = document.getElementById("inputSaveEmployee");//Intanciamos el boton de guardar que contiene ese modal
    var cancel = document.getElementById("inputCancelSaveEmployee");//Intanciamos el boton de cancelar que contiene ese modal
    edit.style.display = "block";//bloqueamos la pantalla para que el usuario no pueda pulsar otra cosa que no sea el modal

    var empleado = getEmployee(this.id);//Obtenemos los datos del empleado que se desea modificar
    var formatoFecha = empleado.fechaNacimiento.substr(0, 4) + "-" + empleado.fechaNacimiento.substr(5, 2) + "-" + empleado.fechaNacimiento.substr(8, 2);
    var id = this.id;

    document.getElementById("nombreEdit").setAttribute("value", empleado.nombre);
    document.getElementById("apellidosEdit").setAttribute("value", empleado.apellidos);
    document.getElementById("telefonoEdit").setAttribute("value", empleado.telefono);
    document.getElementById("fechaNacimientoEdit").setAttribute("value", formatoFecha);
    document.getElementById("departamentoEdit").value = empleado.idDepartamento;

    save.onclick = function () {//Si el usuario confirma los cambios
        var empleado = new Object();
        empleado.idPersona = id;
        empleado.nombre = document.getElementById("nombreEdit").value;
        empleado.apellidos = document.getElementById("apellidosEdit").value;
        empleado.telefono = document.getElementById("telefonoEdit").value;;
        empleado.fechaNacimiento = document.getElementById("fechaNacimientoEdit").value;
        empleado.idDepartamento = document.getElementById("departamentoEdit").value;

        var miLlamada = new XMLHttpRequest();
        miLlamada.open("PUT", "https://localhost:44371/api/PersonasApi/" + id, false);
        miLlamada.setRequestHeader('Content-type', 'application/json');

        if (miLlamada.readyState == 4 && miLlamada.status == 200) {//Si el PUT a sido correcto
            alert("Employee edited!");
            edit.style.display = "none";
            reloadTable();
        }

        miLlamada.send(JSON.stringify(empleado));//Realizamos la llamada
    }

    cancel.onclick = function () {//En caso de cancelación
        edit.style.display = "none";//Quitamos la pantalla del modal
    }
}

//Este función será llamada cada vez que el usuario desee elimninar un empleado de la lista
function removeClick() {
    var id = this.id;
    var modal = document.getElementById("deleteEmployeeModal");
    var acept = document.getElementById("inputAceptDelete");
    var cancel = document.getElementById("inputCancelDelete");
    modal.style.display = "block";

    acept.onclick = function () {
        modal.style.display = "none";

        var llamadaEliminar = new XMLHttpRequest();
        llamadaEliminar.open("DELETE", "https://localhost:44371/api/PersonasApi/" + id);
        llamadaEliminar.onreadystatechange = function () {

            if (llamadaEliminar.readyState == 4 && llamadaEliminar.status == 200) {
                reloadTable();//Actualizamos la tabla
                alert("Employee deleted!");
            }
        }
        llamadaEliminar.send();//Lanzamos la llamada
    }

    cancel.onclick = function () {
        modal.style.display = "none";
    }
}

//Esta función se lanzará cada vez que el usuario pulse el boton de añadir empleado
function insertarEmpleado() {
    var createModal = document.getElementById("addEmployeeModal");//Seleccionamos el modal de la página
    createModal.style.display = "block";

    var id = 1;
    var nombre = document.getElementById("nombre").value;
    var apellidos = document.getElementById("apellidos").value;
    var fechaNacimiento = document.getElementById("fechaNacimiento").value;
    var telefono = document.getElementById("telefono").value;
    var idDepartamento = document.getElementById("departamento").value;

    var empleado = new Object();
    empleado.idPersona = id;
    empleado.nombre = nombre;
    empleado.apellidos = apellidos;
    empleado.fechaNacimiento = fechaNacimiento;
    empleado.telefono = telefono;
    empleado.idDepartamento = idDepartamento;

    insertarEmpleadoPost(empleado);

    function insertarEmpleadoPost(empleado) {//Insertamos el empleado por post
        var llamadaInsertar = new XMLHttpRequest();
        llamadaInsertar.open("POST", "https://localhost:44371/api/PersonasApi", false);
        llamadaInsertar.setRequestHeader('Content-type', 'application/json');

        llamadaInsertar.onreadystatechange = function () {
            if (llamadaInsertar.readyState == 4 && llamadaInsertar.status == 204) {
                alert("Employee inserted!");
                reloadTable();
            }
        }

        llamadaInsertar.send(JSON.stringify(empleado));
    }
}

function addClick() {
    var modal = document.getElementById("addEmployeeModal");
    var acept = document.getElementById("btnAceptAddPerson");
    var cancel = document.getElementById("btnCancelAddPerson");
    modal.style.display = "block";

    acept.onclick = function () {
        insertarEmpleado();
    }

    cancel.onclick = function () {
        modal.style.display = "none";
    }
}