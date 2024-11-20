import React, { useRef, useState } from "react";
import Button from '@mui/material/Button';

function NewItem(props) {
  const [item, setItem] = useState('');
  const [storypoints, setStorypoints] = useState(0); 
  const [priority, setPriority] = useState('');
  const [estimatedHours, setEstimatedHours] = useState(0);
  const [expirationDate, setExpirationDate] = useState();
  const [assigned, setAssigned] = useState('unassigned'); // Estado para el selector

  const formRef = useRef();

  function handleSubmit(e) {
    if (
      !item.trim() || 
      !storypoints || 
      !assigned.trim() || 
      !priority.trim() || 
      !estimatedHours || 
      !expirationDate
    ) {
      return;
    }

    props.addItem({ item, storypoints, assigned, priority, estimatedHours, expirationDate});

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

  function handlePriorityChange(e) {
    setPriority(e.target.value);
  }

  function handleEstimatedHoursChange(e) {
    setEstimatedHours(e.target.value);
  }

  function handleExpirationDateChange(e) {
    setExpirationDate(e.target.value);
  }

  function handleAssignedChange(e) {
    setAssigned(e.target.value);  // Aquí se asegura que el valor seleccionado se guarde en el estado
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
              onKeyDown={(event) => {
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
            <label htmlFor="newPriorityInput">Priority</label>
            <select
              id="newPriorityInput"
              value={priority}
              onChange={handlePriorityChange}
            >
              <option value="">Select a priority</option>
              <option value="Low">Low</option>
              <option value="Mid">Mid</option>
              <option value="High">High</option>
            </select>
          </div>
          <div>
            <label htmlFor="estimatedHours">Estimated Time of Completion</label>
            <input
              id="estimatedHours"
              placeholder="ETC in hours"
              type="number"
              value={estimatedHours}
              onChange={handleEstimatedHoursChange}
            />
          </div>
        </div>
        <div className="newItemFormSection">
          <div>
            <label htmlFor="newAssignedInput">Assigned To</label>
            <select
              id="newAssignedInput"
              value={assigned}
              onChange={handleAssignedChange}
            >
              <option value="unassigned">Select a developer</option>
              <option value="Franco">Franco</option>
              <option value="Facundo">Facundo</option>
              <option value="Saul">Saul</option>
              <option value="Alejandro">Alejandro</option>
            </select>
          </div>
          <div>
            <label htmlFor="NewExpirationDate">Expiration date</label>
            <input
              id="NewExpirationDate"
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
