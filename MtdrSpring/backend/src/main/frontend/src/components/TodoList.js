// components/TodoList.js
import React, { useState, useEffect } from 'react';
import NewItem from '../NewItem';
import API_LIST from '../API';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button, TableBody, CircularProgress } from '@mui/material';
import Moment from 'react-moment';

function TodoList() {
    const [isLoading, setLoading] = useState(false);
    const [isInserting, setInserting] = useState(false);
    const [items, setItems] = useState([]);
    const [error, setError] = useState();
    const [editItemId, setEditItemId] = useState(null);
    const [editItemText, setEditItemText] = useState('');

    function deleteItem(deleteId) {
      fetch(API_LIST+"/"+deleteId, {
        method: 'DELETE',
      })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          const remainingItems = items.filter(item => item.id !== deleteId);
          setItems(remainingItems);
        },
        (error) => {
          setError(error);
        }
      );
    }

    function toggleDone(event, id, description, done) {
      event.preventDefault();
      modifyItem(id, description, done).then(
        (result) => { reloadOneItem(id); },
        (error) => { setError(error); }
      );
    }

    function reloadOneItem(id){
      fetch(API_LIST+"/"+id)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('Something went wrong ...');
          }
        })
        .then(
          (result) => {
            const items2 = items.map(
              x => (x.id === id ? {
                 ...x,
                 'description':result.description,
                 'done': result.done
                } : x));
            setItems(items2);
          },
          (error) => {
            setError(error);
          });
    }

    function modifyItem(id, description, done) {
      var data = {"description": description, "done": done};
      return fetch(API_LIST+"/"+id, {
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
          throw new Error('Something went wrong ...');
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

    function addItem(text){
      setInserting(true);
      var data = {};
      data.description = text;
      fetch(API_LIST, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),
      }).then((response) => {
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      }).then(
        (result) => {
          var id = result.headers.get('location');
          var newItem = {"id": id, "description": text}
          setItems([newItem, ...items]);
          setInserting(false);
        },
        (error) => {
          setInserting(false);
          setError(error);
        }
      );
    }

    useEffect(() => {
      setLoading(true);
      fetch(API_LIST)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('Something went wrong ...');
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

    return (
      <div className="App">
        <NewItem addItem={addItem} isInserting={isInserting}/>
        { error &&
          <p>Error: {error.message}</p>
        }
        { isLoading &&
          <CircularProgress />
        }
        { !isLoading &&
        <div id="maincontent">
          <table id="itemlistNotDone" className="itemlist">
            <TableBody>
            {items.map(item => (
              !item.done && (
              <tr key={item.id}>
                <td className="description">
                {editItemId === item.id ? (
                  <input
                    type="text"
                    value={editItemText}
                    onChange={(e) => setEditItemText(e.target.value)}
                    onKeyDown={handleKeyDown}
                    />
                  ) : (
                    item.description
                  )}
                </td>
                <td className="date"><Moment format="MMM Do hh:mm:ss">{item.createdAt}</Moment></td>
                <td><Button variant="contained" className="DoneButton" onClick={(event) => enableEdit(item)} size="small">
                      Edit
                    </Button></td>
                <td><Button variant="contained" className="EditButton" onClick={(event) => toggleDone(event, item.id, item.description, !item.done)} size="small">
                      Done
                    </Button></td>
              </tr>
            )))}
            </TableBody>
          </table>
          <h2 id="donelist">
            Done items
          </h2>
          <table id="itemlistDone" className="itemlist">
            <TableBody>
            {items.map(item => (
              item.done && (
              <tr key={item.id}>
                <td className="description">{item.description}</td>
                <td className="date"><Moment format="MMM Do hh:mm:ss">{item.createdAt}</Moment></td>
                <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item.id, item.description, !item.done)} size="small">
                      Undo
                    </Button></td>
                <td><Button startIcon={<DeleteIcon />} variant="contained" className="DeleteButton" onClick={() => deleteItem(item.id)} size="small">
                      Delete
                    </Button></td>
              </tr>
            )))}
            </TableBody>
          </table>
        </div>
        }
      </div>
    );
}

export default TodoList;