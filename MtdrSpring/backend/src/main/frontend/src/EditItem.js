import React, { useState, useEffect } from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import { API_USERS } from './API';

function EditItem(props) {
    // Estados para los tres campos: descripción, storypoints, responsable
    const [newDescription, setNewDescription] = useState(props.description);
    const [newStorypoints, setNewStorypoints] = useState(props.storypoints);
    const [newAssigned, setNewAssigned] = useState(props.assigned);
    const [newPriority, setNewPriority] = useState(props.priority);
    const [newEstimated_Hours, setNewEstimated_Hours] = useState(props.priority);
    const [newExpirationTS, setNewExpirationTS] = useState(
        props.expirationTS ? new Date(props.expirationTS).toISOString().split('T')[0] : ''
    );
    const [developers, setDevelopers] = useState([]);

    useEffect(() => {
        fetch(API_USERS)
            .then((response) => response.json()) 
            .then((data) => {
                setDevelopers(data); // Guarda la lista de desarrolladores en el estado
            })
            .catch((error) => {
                console.error("Error fetching developers:", error);
            });
    }, []);


    const handleEdit = (e) => {
        e.preventDefault();
    
        if (!newDescription.trim()) {
            alert("Description cant be empty");
            return;
        }
    
        if (newStorypoints === "" || isNaN(newStorypoints)) {
            alert("Storypoints must be a numeric value");
            return;
        }

        if (!newAssigned.trim()) {
            alert("Assigned field can't be empty");
            return;
        }

        if (!newPriority.trim()) {
            alert("Priority field can't be empty");
            return;
        }

        if (newEstimated_Hours === "" || isNaN(newEstimated_Hours)) {
            alert("Estimated hours must be a numeric value");
            return;
        }

        if (!newExpirationTS) {
            alert("Expiration date can't be empty");
            return;
        }
    
    
        props.updateItem(props.id, {
            description: newDescription.trim(),
            storypoints: Number(newStorypoints),
            assigned: newAssigned.trim(),
            priority: newPriority.trim(),
            estimated_Hours: Number(newEstimated_Hours),
            expiration_TS: new Date(newExpirationTS).toISOString()
        });
    
        props.onClose();
    };
    

    return (
        <div>
            <form onSubmit={handleEdit}  style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <TextField
                    label="Description"
                    variant="outlined"
                    value={newDescription}
                    onChange={(e) => setNewDescription(e.target.value)}
                    fullWidth
                />
                
                <TextField
                    label="Story Points"
                    type="number"
                    variant="outlined"
                    value={newStorypoints}
                    onChange={(e) => setNewStorypoints(e.target.value)}
                    fullWidth
                />
                
                {/* Selector para "Assigned To" */}
                <FormControl fullWidth>
                    <InputLabel id="assigned-to-label">Assigned To</InputLabel>
                    <Select
                        labelId="assigned-to-label"
                        value={newAssigned}
                        onChange={(e) => setNewAssigned(e.target.value)}
                        label="Assigned To"
                    >
                        {developers.length > 0 ? (
                            developers.map((developer) => (
                                <MenuItem key={developer.id} value={developer.name}>
                                    {developer.name}
                                </MenuItem>
                            ))
                        ) : (
                            <MenuItem disabled>No developers available</MenuItem>
                        )}
                    </Select>
                </FormControl>

                <FormControl fullWidth>
                    <InputLabel id="priority-label">Priority</InputLabel>
                    <Select
                        labelId="priority-label"
                        value={newPriority}
                        onChange={(e) => setNewPriority(e.target.value)}
                        label="Priority"
                    >
                        <MenuItem value="Low">Low</MenuItem>
                        <MenuItem value="Mid">Mid</MenuItem>
                        <MenuItem value="High">High</MenuItem>
                    </Select>
                </FormControl>

                <TextField
                    label="Estimated Hours"
                    type="number"
                    variant="outlined"
                    value={newEstimated_Hours}
                    onChange={(e) => setNewEstimated_Hours(e.target.value)}
                    fullWidth
                />

                <TextField
                    label="Expiration Date"
                    type="date"
                    variant="outlined"
                    value={newExpirationTS}
                    onChange={(e) => setNewExpirationTS(e.target.value)}
                    fullWidth
                    InputLabelProps={{
                        shrink: true,
                    }}
                />

                <Button type="submit" variant="contained">Guardar</Button>
                <Button onClick={props.onClose} variant="outlined">Cancelar</Button>
            </form>
        </div>
    );
}

export default EditItem;