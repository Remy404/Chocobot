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

import React, { useRef, useState } from "react";
import Button from '@mui/material/Button';

function NewItem(props) {
  // Se agregan dos estados: uno para 'item' y otro para 'storypoints'
  const [item, setItem] = useState('');
  const [storypoints, setStorypoints] = useState(); // Nuevo estado para storypoints
  const [responsable, setResponsable] = useState('');
  const [priority, setPriority] = useState();
  const [estimatedHours, setEstimatedHours] = useState();
  const [expirationDate, setExpirationDate] = useState();

  const formRef = useRef();

  // Modificamos el handleSubmit para manejar tanto item como storypoints
  function handleSubmit(e) {
    if (!item.trim() || !storypoints.trim() || !responsable.trim() || !priority.trim() || !estimatedHours.trim() || !expirationDate.trim()) { // Validar ambos campos
      return;
    }
    // addItem ahora recibe un objeto con 'item' y 'storypoints'
    props.addItem({ item, storypoints, responsable, priority, estimatedHours, expirationDate });

    if (formRef.current) {
      formRef.current.reset();
    }
  }

  function handleItemChange(e) {
    setItem(e.target.value);
  }

  function handleStorypointsChange(e) {
    setStorypoints(e.target.value);
  }

  function handleResponsableChange(e){
    setResponsable(e.target.value);
  }

  function handlePriorityChange(e) {
    setPriority(e.target.value);
  }

  function handleEstimatedHoursChange(e) {
    setEstimatedHours(e.target.value);
  }

  function handleExpirationDateChange(e) {
    setExpirationDate(e.target.value)
  }

  return (
    <div id="newinputform">
      <form ref={formRef} className="newItemForm">
        <div className="newItemFormSection">
          <div>
            <label htmlFor="newiteminput">Task Name</label>
            <input
              id="newiteminput"
              placeholder="Task Name"
              type="text"
              autoComplete="off"
              value={item}
              onChange={handleItemChange}
              onKeyDown={event => {
                if (event.key === 'Enter') {
                  handleSubmit(event);
                }
              }}
            />
          </div>
          <div>
            <label htmlFor="newstorypointsinput">Number of story points</label>
            <input
              id="newstorypointsinput"
              placeholder="Storypoints"
              type="number"
              value={storypoints}
              onChange={handleStorypointsChange}
            />
          </div>
        </div>
        <div className="newItemFormSection">
          <div>
            <label htmlFor="newresponsableinput">Priority</label>
            <input
              id="newresponsableinput"
              placeholder="Priority"
              type="text"
              value={priority}
              onChange={handlePriorityChange}
            />
          </div>
          <div>
            <label htmlFor="estimatedhours">Estimated Time of Completion</label>
            <input
              id="estimatedhours"
              placeholder="ETC in hours"
              type="number"
              value={estimatedHours}
              onChange={handleEstimatedHoursChange}
            />
          </div>
        </div>
        <div className="newItemFormSection">          
          <div>
            <label htmlFor="newresponsableinput">Responsable</label>
            <input
              id="newresponsableinput"
              placeholder="Responsable"
              type="text"
              value={responsable}
              onChange={handleResponsableChange}
            />
          </div>
          <div>
            <label htmlFor="expirationdate">Expiration date</label> 
            <input
              id="expirationdate"
              placeholder="Expiration date"
              type="date"
              value={expirationDate}
              onChange={handleExpirationDateChange}
            />
          </div>    
        </div>
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
