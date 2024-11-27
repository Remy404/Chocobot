import React, { useState, useEffect } from "react";
import Button from '@mui/material/Button';
import { API_USERS } from './API';

function NewItem(props) {
  const [item, setItem] = useState('');
  const [storypoints, setStorypoints] = useState();
  const [responsable, setResponsable] = useState('');
  const [priority, setPriority] = useState('');
  const [estimatedHours, setEstimatedHours] = useState();
  const [expirationDate, setExpirationDate] = useState();
  const [developers, setDevelopers] = useState([]);

  useEffect(() => {
    // Hacer la solicitud HTTP usando el nuevo endpoint API_USERS
    fetch(API_USERS)
      .then((response) => response.json()) // Asegúrate de parsear la respuesta a JSON
      .then((data) => {
        setDevelopers(data); // Suponiendo que la respuesta es un array de desarrolladores
      })
      .catch((error) => {
        console.error("Error fetching developers:", error);
      });
  }, []);

  function handleSubmit(e) {
    e.preventDefault();

    if (!item.trim() || !storypoints.trim() || !responsable.trim() || !priority.trim() || !estimatedHours.trim() || !expirationDate.trim()) {
      return;
    }
    props.addItem({ item, storypoints, responsable, priority, estimatedHours, expirationDate });

    setItem('');
    setResponsable('');
    setStorypoints('');
    setPriority('');
    setExpirationDate('');
    setEstimatedHours('');
  }

  function handleItemChange(e) {
    setItem(e.target.value);
  }

  function handleStorypointsChange(e) {
    setStorypoints(e.target.value);
  }

  function handleResponsableChange(e) {
    setResponsable(e.target.value);
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

  return (
    <div id="newinputform">
      <form className="newItemForm">
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
            <label htmlFor="newpriorityinput">Priority</label>
            <select name="priority" id="newpriorityinput" onChange={handlePriorityChange} value={priority}>
              <option value="" disabled>Select</option>
              <option value="Low">Low</option>
              <option value="Mid">Mid</option>
              <option value="High">High</option>
            </select>
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
            <label htmlFor="newresponsableinput">Assigned</label>
            <select
              id="newresponsableinput"
              value={responsable}
              onChange={handleResponsableChange}
            >
              <option value="" disabled>Select a Developer</option>
              {developers.length > 0 ? (
                developers.map((developer) => (
                  <option key={developer.id} value={developer.name}>
                    {developer.name}
                  </option>
                ))
              ) : (
                <option disabled>No developers available</option>
              )}
            </select>
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