import { Pie, Bar, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  BarElement,
  PointElement,
  LineElement,
} from "chart.js";
import { useEffect, useState } from "react";

ChartJS.register(
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  BarElement,
  PointElement,
  LineElement
);

function Estadisticas(props) {
  const [completedCount, setCompletedCount] = useState(0);
  const [pendingCount, setPendingCount] = useState(0);
  const [storyPointsData, setStoryPointsData] = useState({ labels: [], values: [] });

  useEffect(() => {
    const tasks = props.tasks;
    if (tasks.length === 0) {
        return;
      }

      const completed = tasks.filter(task => task.done === true).length;
      const pending = tasks.filter(task => task.done === false).length;
      setCompletedCount(completed);
      setPendingCount(pending);

      const storyPointsByDeveloper = tasks.reduce((acc, task) => {
        if (task.assigned && task.storyPoints) {
          acc[task.assigned] = (acc[task.assigned] || 0) + task.storyPoints;
        }
        return acc;
      }, {});

      const labels = Object.keys(storyPointsByDeveloper);
      const values = Object.values(storyPointsByDeveloper);
      setStoryPointsData({ labels, values });
  }, [props.tasks]);

  const pieData = {
    labels: ["Completadas", "Pendientes"],
    datasets: [
      {
        label: "Tareas",
        data: [completedCount, pendingCount],
        backgroundColor: ["rgba(255, 99, 132, 0.6)", "rgba(255, 159, 64, 0.6)"],
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
      },
    },
  };

  const barData = {
    labels: storyPointsData.labels.length > 0 ? storyPointsData.labels : ["Developer A", "Developer B"],
    datasets: [
      {
        label: "StoryPoints finished per developer",
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
      },
      title: {
        display: true,
      },
    },
  };

  // Gráfica de líneas hardcodeada
  const lineData = {
    labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo"],
    datasets: [
      {
        label: "Tareas Creadas",
        data: [12, 19, 7, 15, 10],
        borderColor: "rgba(54, 162, 235, 1)",
        backgroundColor: "rgba(54, 162, 235, 0.5)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const lineOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Tareas Creadas por Mes",
      },
    },
  };

  return (
    <div style={{ display: 'flex', gap: '40px', justifyContent: 'center', alignItems: 'center', marginTop: '30px' }}>
      <div>
        <h3>Pending Tasks vs Completed Tasks</h3>
        <div style={{ height: "400px" }}>
          <Pie data={pieData} options={pieOptions} />
        </div>
      </div>
      <div>
        <h3>StoryPoints finished per developer</h3>
        <div style={{ height: "400px" }}>
          <Bar data={barData} options={barOptions} />
        </div>
      </div>
      <div>
        <h2>Tareas Creadas por Mes</h2>
        <div style={{ height: "400px" }}>
          <Line data={lineData} options={lineOptions} />
        </div>
      </div>
    </div>
  );
}

export default Estadisticas;
