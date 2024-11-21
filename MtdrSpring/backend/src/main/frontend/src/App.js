/*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * This is the application main React component. We're using "function"
 * components in this application. No "class" components should be used for
 * consistency.
 * @author  jean.de.lavarene@oracle.com
 */

import React, { useState, useEffect } from 'react';
import NewItem from './NewItem';
import API_LIST from './API';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button, CircularProgress, Accordion, AccordionSummary, AccordionDetails, Typography } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Moment from 'react-moment';
import Estadisticas from './components/Estadisticas';

/* In this application we're using Function Components with the State Hooks
 * to manage the states. See the doc: https://reactjs.org/docs/hooks-state.html
 * This App component represents the entire app. It renders a NewItem component
 * and two tables: one that lists the todo items that are to be done and another
 * one with the items that are already done.
 */

function App() {
    const [isLoading, setLoading] = useState(false);
    const [isInserting, setInserting] = useState(false);
    const [items, setItems] = useState([]);
    const [error, setError] = useState();
    const [editItemId, setEditItemId] = useState(null);
    const [editItemText, setEditItemText] = useState('');

    // Extraer todos los responsables únicos de la lista de items
    const [selectedDeveloper, setSelectedDeveloper] = useState(''); // Estado para almacenar el desarrollador seleccionado
    const uniqueDevelopers = [...new Set(items.map(item => item.assigned))];

    const [selectedSprint, setSelectedSprint] = useState('');
    const sprints = [...new Set(items.map(item => formatDate(item.expiration_TS)))];

    function deleteItem(deleteId) {
      fetch(API_LIST + "/" + deleteId, {
        method: 'DELETE',
      })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ...deleteItem');
        }
      })
      .then(
        () => {
          const remainingItems = items.filter(item => item.id !== deleteId);
          setItems(remainingItems);
        },
        (error) => {
          setError(error);
        }
      );
    }

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [year, month, day].join('-');
    }

    function toggleDone(event, id, done) {
      event.preventDefault();

      changeItemState(id, done).then(
          () => { reloadOneItem(id); },
          (error) => { setError(error); }
      );
    }

    function reloadOneItem(id) {
      fetch(API_LIST + "/" + id)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('Something went wrong ...reloadOneItem');
          }
        })
        .then(
          (result) => {
            const items2 = items.map(
              x => (x.id === id ? {
                ...x,
                'description': result.description,
                'done': result.done,
                'assigned': result.assigned,
                'finished_TS': result.finished_TS,
              } : x));
            setItems(items2);
          },
          (error) => {
            setError(error);
          });
    }

    function changeItemState(id, done) {
        return fetch(API_LIST + `/${id}/done`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "done": done,
            })
        })
        .then(response => {
            if (response.ok) {
                return response;
            } else {
                throw new Error('Something went wrong ... markItemDone');
            }
        });
    }

    function modifyItem(id, description, done, assigned) {
      var data = {
          "description": description,
          "done": done,
          "assigned": assigned
      };

      return fetch(API_LIST + "/" + id, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ... modifyItem');
        }
      });
    }

    function enableEdit(item) {
      setEditItemId(item.id);
      setEditItemText(item.description);
    }

    function saveEdit() {
      if (editItemId) {
        modifyItem(editItemId, editItemText, false).then(() => {
          setEditItemId(null);
          setEditItemText('');
          reloadOneItem(editItemId);
        }).catch(error => {
          setError(error);
        });
      }
    }

    function handleKeyDown(event) {
      if (event.key === 'Enter') {
        saveEdit();
      }
    }

    /*
    To simulate slow network, call sleep before making API calls.
    const sleep = (milliseconds) => {
      return new Promise(resolve => setTimeout(resolve, milliseconds))
    }
    */

    useEffect(() => {
      setLoading(true);
      fetch(API_LIST)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('Something went wrong ... useEffect');
          }
        })
        .then(
          (result) => {
            setLoading(false);
            setItems(result);
          },
          (error) => {
            setLoading(false);
            setError(error);
          });
    }, []);

    function addItem(newItem) {  // newItem ahora es un objeto con 'item' y 'storypoints'
      setInserting(true);
      
      // Estructura de datos con la descripción y los storypoints
      var data = { 
        description: newItem.item,      // Descripción del ítem
        storyPoints: newItem.storypoints,  // Puntos de historia
        assigned: newItem.responsable,
        priority: newItem.priority,
        estimated_Hours: parseInt(newItem.estimatedHours),
        expiration_TS: new Date(newItem.expirationDate).toISOString(),
        done: false,
      };
    
      fetch(API_LIST + "/add", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),  // Enviar la descripción y los storypoints
      })
      .then((result) => {
          if (!result.ok) {
              return;
          }

          var id = result.headers.get('location');
          var newItemWithId = { 
            "id": id, 
            "description": newItem.item,  // Descripción
            "storyPoints": newItem.storypoints,  // Puntos de historia
            "assigned": newItem.responsable,
            "priority": newItem.priority,
            "done": newItem.done,
            "estimated_Hours": newItem.estimatedHours,
            "expiration_TS": newItem.expirationDate,
          };
          setItems([newItemWithId, ...items]);
        }
      ).catch((e) => {
        console.error(e);
      }).finally(() => {
        setInserting(false);
      });
    }
    
    return (
      <div className="App" style={{ padding: '60px' }}>
        <h1>ChocoLabs</h1>
        <NewItem addItem={addItem} isInserting={isInserting}/>

        { error && <p>Error: {error.message}</p> }
        { isLoading && <CircularProgress style={{ marginTop: '10px' }} /> }

        { !isLoading &&
          <div id="maincontent">
            {/* Selector para filtrar tareas por desarrollador */}
            <div className="filters-section">
              <div>
                  <label htmlFor="developer-select">Filter by developer </label>
                  <select
                    id="developer-select"
                    value={selectedDeveloper}
                    onChange={(e) => setSelectedDeveloper(e.target.value)}
                  >
                    <option value="">All</option>
                    {uniqueDevelopers.map((developer, index) => (
                      <option key={index} value={developer}>
                        {developer}
                      </option>
                    ))}
                  </select>
              </div>
              <div>
                  <label htmlFor="sprint-select">Filter by Sprint</label>
                  <select
                      name="sprint"
                      id="sprint-select"
                      value={selectedSprint}
                      onChange={(e) => setSelectedSprint(e.target.value)}
                  >
                      <option value="">All</option>
                      {sprints.map((date, index) => (
                          <option key={index} value={date}>Sprint {index + 1}</option>
                      ))}
                  </select>
              </div>
            </div>
            
            {/* Sección de Tareas pendientes */}
            <h2>Pending Tasks</h2>
            {items
            .filter(item => {
                const matchesDeveloper = selectedDeveloper === "" || item.assigned === selectedDeveloper;
                const matchesSprint = selectedSprint === "" || formatDate(item.expiration_TS) === selectedSprint;
                return !item.done && matchesDeveloper && matchesSprint;
            }).map(item => (
              <Accordion key={item.id}>
                <AccordionSummary
                  expandIcon={<ExpandMoreIcon />}
                  aria-controls={`panel${item.id}-content`}
                  id={`panel${item.id}-header`}
                >
                  <Typography>{editItemId === item.id ? (
                    <input
                      type="text"
                      value={editItemText}
                      onChange={(e) => setEditItemText(e.target.value)}
                      onKeyDown={handleKeyDown}
                    />
                  ) : (
                    item.description
                  )}</Typography>
                </AccordionSummary>
                <AccordionDetails>
                  <Typography>
                    Storypoints: {item.storyPoints} {/* Mostrar los Storypoints */}
                  </Typography>
                  <Typography>
                    Assigned to: {item.assigned}  {/* Mostrar el responsable */}
                  </Typography>
                  <Typography>
                    Priority: {item.priority}
                  </Typography>
                  <Typography>
                    Estimated Hours: {item.estimated_Hours}
                  </Typography>
                    <Typography>
                        Expiration Date: <Moment format="MMM Do hh:mm:ss">{new Date(item.expiration_TS)}</Moment>
                    </Typography>
                  <Button style={{ marginRight: "10px" }} variant="contained" onClick={() => enableEdit(item)} size="small">
                    Edit
                  </Button>
                  <Button variant="contained" onClick={(event) => toggleDone(event, item.id, !item.done)} size="small">
                    Done
                  </Button>
                </AccordionDetails>
              </Accordion>
            ))}

            {/* Sección de Tareas completadas */}
            <h2 style={{ marginTop: "30px" }}>Completed Tasks</h2>
            {items.filter(item => {
                const matchesDeveloper = selectedDeveloper === "" || item.assigned === selectedDeveloper;
                const matchesSprint = selectedSprint === "" || formatDate(item.expiration_TS) === selectedSprint;
                return item.done && matchesDeveloper && matchesSprint;
            }).map(item => (
              <Accordion key={item.id}>
                <AccordionSummary
                  expandIcon={<ExpandMoreIcon />}
                  aria-controls={`panel${item.id}-content`}
                  id={`panel${item.id}-header`}
                >
                  <Typography>{item.description}</Typography>
                </AccordionSummary>
                <AccordionDetails>
                  <Typography>
                    Storypoints: {item.storyPoints} {/* Mostrar los Storypoints */}
                  </Typography>
                  <Typography>
                    Assigned To: {item.assigned}  {/* Mostrar el responsable */}
                  </Typography>
                  <Typography>
                    Priority: {item.priority}
                  </Typography>
                  <Typography>
                    Estimated Hours: {item.estimated_Hours}
                  </Typography>
                  <Typography>
                    Completed at: <Moment format="MMM Do hh:mm:ss">{new Date(item.finished_TS)}</Moment>
                  </Typography>
                  <Button style={{ marginRight: "10px" }} variant="contained" onClick={(event) => toggleDone(event, item.id, !item.done)} size="small">
                    Undo
                  </Button>
                  <Button startIcon={<DeleteIcon />} variant="contained" onClick={() => deleteItem(item.id)} size="small">
                    Erase
                  </Button>
                </AccordionDetails>
              </Accordion>
            ))}
            <h2 style={{ marginTop: "30px" }}>Project Statistics</h2>
            <Estadisticas tasks={items} />
          </div>
        }
      </div>
    );
}

export default App;
