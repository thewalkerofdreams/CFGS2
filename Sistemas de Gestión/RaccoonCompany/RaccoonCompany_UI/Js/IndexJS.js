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
    cargarListadoPersonas();
}

function cargarListadoPersonas() {
    var miLlamada = new XMLHttpRequest();
    miLlamada.open("GET", "https://crudpersonasui-victor.azurewebsites.net/api/personasapi");

    //Definicion estados
    miLlamada.onreadystatechange = function () {


        if (miLlamada.readyState < 4) {
            //aquí se puede poner una imagen de un reloj o un texto “Cargando”
            document.getElementById("divApellidos").innerHTML = "Cargando...";

        }
        else
            if (miLlamada.readyState == 4 && miLlamada.status == 200) {
                var arrayPersonas = JSON.parse(miLlamada.responseText);
                //var persona = arrayPersonas[0]; 
                //document.getElementById("divApellidos").innerHTML = persona.apellidos;

                for (i = 0; i < arrayPersonas.length; i++) {
                    var td = document.createElement('td');//Creamos un tag de <td>
                    td.innerHTML = "" + arrayPersonas[i].nombre + "";
                    document.getElementById("EmployeeFirstName").appendChild(td);//Agregamos el tag <td> a la lista (<tr>)
                    td.innerHTML = "" + arrayPersonas[i].apellidos + "";
                    document.getElementById("EmployeeLastName").appendChild(td);//Agregamos el tag <td> a la lista (<tr>)
                }
            }

    };

    miLlamada.send();
}