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
}

/*function cargarListadoPersonas() {
    var miLlamada = new XMLHttpRequest();
    var miLlamada2 = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/personasapi");
    miLlamada2.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/departamentosapi");

    //Definicion estados
    miLlamada.onreadystatechange = function () {

        
            if (miLlamada.readyState == 4 && miLlamada.status == 200 && miLlamada2.readyState == 4 && miLlamada2.status == 200) {
                var arrayPersonas = JSON.parse(miLlamada.responseText);
                //var persona = arrayPersonas[0]; 
                //document.getElementById("divApellidos").innerHTML = persona.apellidos;

                for (i = 0; i < arrayPersonas.length; i++) {
                    var td = document.createElement('td');//Creamos un tag de <td>
                    td.innerHTML = "" + arrayPersonas[i].nombre + "";
                    document.getElementById("EmployeeFirstName").appendChild(td);//Agregamos el tag <td> a la lista (<tr>)
                    td.innerHTML = "" + arrayPersonas[i].apellidos + "";
                    document.getElementById("EmployeeLastName").appendChild(td);//Agregamos el tag <td> a la lista (<tr>)
                    td.innerHTML = "" + arrayPersonas[i].fechaNacimiento + "";
                    document.getElementById("EmployeeBirthday").appendChild(td);//Agregamos el tag <td> a la lista (<tr>)
                    var arrayDepartamentos = JSON.parse(miLlamada2.responseText);
                    Boolean nombreEncontrado = false;
                    for (i = 0; i < arrayDepartamentos.length && !nombreEncontrado; i++) {
                        if (arrayDepartamentos[i].id == arrayPersonas[i].idDepartamento) {
                            td.innerHTML = "" + arrayDepartamentos[i].nombre + "";
                            nombreEncontrado = true;
                        }
                    }
                    document.getElementById("EmployeeDepartament").appendChild(td);//Agregamos el tag <td> a la lista (<tr>)
                }
            

    };

    miLlamada.send();
    miLlamada2.send();
    }*/

    function cargarListadoPersonasConDepartamento(){
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

    function cargarListadoPersonas(arrayDepartamentos){
        var miLlamada = new XMLHttpRequest();
        miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/personasapi");

        //Definicion estados
        miLlamada.onreadystatechange = function () {
           
            if (miLlamada.readyState == 4 && miLlamada.status == 200) {
                var arrayPersonas = JSON.parse(miLlamada.responseText);

                for (i = 0; i < arrayPersonas.length; i++) {
                    var tr = document.createElement('tr');
                    tr.id = "nuevoTr";
                    document.getElementById("tBodyEmployee").appendChild(tr);
                     var td = document.createElement('td');//Creamos un tag <td>
                    var td2 = document.createElement('td');//Creamos un tag <td>
                    var td3 = document.createElement('td');//Creamos un tag <td>
                    var td4 = document.createElement('td');//Creamos un tag <td>
                    for (i = 0;  i < arrayPersonas.length; i++) {
                         td.innerHTML = "" + arrayPersonas[i].nombre + "";
                        //document.getElementById("EmployeeFirstName").appendChild(td);//Introducimos el nombre del empleado en la lista
                        document.getElementById("nuevoTr").appendChild(td);
                        td2.innerHTML = "" + arrayPersonas[i].apellidos + "";
                        //document.getElementById("EmployeeLastName").appendChild(td2);//Introducimos el apellido del empleado en la lista
                        document.getElementById("nuevoTr").appendChild(td2);
                        td3.innerHTML = "" + arrayPersonas[i].fechaNacimiento + "";
                        //document.getElementById("EmployeeBirthday").appendChild(td3);//Introducimos la fecha de nacimiento del empleado en la lista
                        document.getElementById("nuevoTr").appendChild(td3);
                        var nombreEncontrado = false;
                        for (j = 0; i < arrayDepartamentos.length && !nombreEncontrado; j++) {
                            if (arrayDepartamentos[j].id == arrayPersonas[i].idDepartamento) {
                                td4.innerHTML = "" + arrayDepartamentos[j].nombre + "";
                                nombreEncontrado = true;
                            }
                        }
                        //document.getElementById("EmployeeDepartament").appendChild(td4);//Agregamos el tag <td> a la lista (<tr>)
                        document.getElementById("nuevoTr").appendChild(td4);
                       
                    }
                } 
            }
        };

        miLlamada.send();
    }