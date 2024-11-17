import { Pie, Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  BarElement,
} from "chart.js";
import { useEffect, useState } from "react";

ChartJS.register(CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement, BarElement);

function Estadisticas() {
  const [completedCount, setCompletedCount] = useState(0);
  const [pendingCount, setPendingCount] = useState(0);
  const [storyPointsData, setStoryPointsData] = useState({ labels: [], values: [] });

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch("/todolist"); // Usa API_LIST aquí si es necesario
      const tasks = await response.json();

      // Calcular tareas completadas y pendientes
      const completed = tasks.filter(task => task.done === true).length;
      const pending = tasks.filter(task => task.done === false).length;
      setCompletedCount(completed);
      setPendingCount(pending);

      // Calcular story points por desarrollador
      const storyPointsByDeveloper = tasks.reduce((acc, task) => {
        if (task.assigned && task.storyPoints) {
          acc[task.assigned] = (acc[task.assigned] || 0) + task.storyPoints;
        }
        return acc;
      }, {});

      const labels = Object.keys(storyPointsByDeveloper);
      const values = Object.values(storyPointsByDeveloper);
      setStoryPointsData({ labels, values });
    };

    fetchData();
  }, []);

  const pieData = {
    labels: ["Completadas", "Pendientes"],
    datasets: [
      {
        label: "Tareas",
        data: [completedCount, pendingCount],
        backgroundColor: [
          "rgba(255, 99, 132, 0.6)",
          "rgba(255, 159, 64, 0.6)"
        ],
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
        color: "white"
      },
      title: {
        display: true,
        color: "white",
      },
    },
  };

  const barData = {
    labels: storyPointsData.labels.length > 0 ? storyPointsData.labels : ["Developer A", "Developer B"],
    datasets: [
      {
        label: "Story Points por Desarrollador",
        data: storyPointsData.values.length > 0 ? storyPointsData.values : [5, 3],
        backgroundColor: "rgba(75, 192, 192, 0.6)",
      },
    ],
  };

  const barOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
        color: "white",
      },
      title: {
        display: true,
        color: "white",
      },
    },
  };

  return (
    <div style={{ display: 'flex', gap: '20px', justifyContent: 'center', alignItems: 'center', marginTop: '30px' }}>
      <div>
        <h2>Pending Tasks to Completed Tasks</h2>
        <div style={{ height: "400px" }}>
          <Pie data={pieData} options={pieOptions}/>
        </div>
      </div>
      <div>
        <h2>Story Points por Desarrollador</h2>
        <div style={{ height: "400px" }}>
          <Bar data={barData} options={barOptions}/>
        </div>
      </div>
    </div>
  );
}

export default Estadisticas;
