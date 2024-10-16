/*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * Component that supports creating a new todo item.
 * @author  jean.de.lavarene@oracle.com
 */

import React, { useState } from "react";
import Button from '@mui/material/Button';

function NewItem(props) {
  // Se agregan dos estados: uno para 'item' y otro para 'storypoints'
  const [item, setItem] = useState('');
  const [storypoints, setStorypoints] = useState(0); // Nuevo estado para storypoints
  const [responsable, setResponsable] = useState('')

  // Modificamos el handleSubmit para manejar tanto item como storypoints
  function handleSubmit(e) {
    if (!item.trim() || !storypoints.trim() || !responsable.trim()) { // Validar ambos campos
      return;
    }
    // addItem ahora recibe un objeto con 'item' y 'storypoints'
    props.addItem({ item, storypoints, responsable });
    setItem(""); // Reiniciar el campo del item
    setStorypoints(""); // Reiniciar el campo de storypoints
    setResponsable("");
    e.preventDefault();
  }

  // Maneja el cambio del campo 'item'
  function handleItemChange(e) {
    setItem(e.target.value);
  }

  // Maneja el cambio del campo 'storypoints'
  function handleStorypointsChange(e) {
    setStorypoints(e.target.value);
  }

  function handleResponsableChange(e){
    setResponsable(e.target.value)
  }

  return (
    <div id="newinputform">
      <form>
        <input
          id="newiteminput"
          placeholder="Titulo de Tarea"
          type="text"
          autoComplete="off"
          value={item}
          onChange={handleItemChange}
          // Añade la funcionalidad de 'Enter' para agregar un ítem
          onKeyDown={event => {
            if (event.key === 'Enter') {
              handleSubmit(event);
            }
          }}
        />
        <span>&nbsp;&nbsp;</span>
        {/* Nuevo campo de entrada para Storypoints */}
        <input
          id="newstorypointsinput"
          placeholder="Storypoints"
          type="number" // Asegurarse de que el campo sea numérico
          value={storypoints}
          onChange={handleStorypointsChange}
        />
        <input
          id="newresponsableinput"
          placeholder="Responsable"
          type="text" // Asegurarse de que el campo sea texto
          value={responsable}
          onChange={handleResponsableChange}
        />
        <span>&nbsp;&nbsp;</span>
        <Button
          className="AddButton"
          variant="contained"
          disabled={props.isInserting}
          onClick={!props.isInserting ? handleSubmit : null}
          size="small"
        >
          {props.isInserting ? 'Adding…' : 'Add'}
        </Button>
      </form>
    </div>
  );
}

export default NewItem;
