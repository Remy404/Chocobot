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
  Filler
} from "chart.js";
import { useEffect, useState } from "react";

ChartJS.register(CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement, BarElement, PointElement, LineElement, Filler);

function Estadisticas(props) {
  const [completedCount, setCompletedCount] = useState(0);
  const [pendingCount, setPendingCount] = useState(0);
  const [storyPointsData, setStoryPointsData] = useState({ labels: [], values: [] });
  const [completedTasksSprintData, setCompletedTasksData] = useState({ labels: [], values: [] });
  const [developerProductivityData, setDeveloperProductivityData] = useState({ labels: [], values: [] });

  useEffect(() => {
    const tasks = props.tasks;
    if (tasks.length === 0) {
      return;
    }

    // Calculate completed and pending tasks
    const completed = tasks.filter((task) => task.done === true).length;
    const pending = tasks.filter((task) => task.done === false).length;
    setCompletedCount(completed);
    setPendingCount(pending);

    // Calculate story points by developer
    const storyPointsByDeveloper = tasks.reduce((acc, task) => {
      if (task.assigned && task.storyPoints) {
        acc[task.assigned] = (acc[task.assigned] || 0) + task.storyPoints;
      }
      return acc;
    }, {});
    setStoryPointsData({ labels: Object.keys(storyPointsByDeveloper), values: Object.values(storyPointsByDeveloper) });

    // Calculate tasks completed tasks per sprint
    const completedTasksPerSprint = new Map();
    tasks.forEach((task) => {
      const date = new Date(task.expiration_TS);
      const key = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;

      if (!completedTasksPerSprint.has(key)) {
        completedTasksPerSprint.set(key, 1);
      } else {
        completedTasksPerSprint.set(key, completedTasksPerSprint.get(key) + 1);
      }
    });
    setCompletedTasksData({
      labels: Array.from(completedTasksPerSprint.keys()).map((_, i) => {
        return `Sprint ${i + 1}`
      }),
      values: Array.from(completedTasksPerSprint.values()),
    });

    // Calculate developer productivity
    const developers = new Map();
    let totalStoryPoints = 0;
    let totalCompletedStoryPoints = 0;
    tasks.forEach((task) => {
      if (!developers.has(task.assigned)) {
        developers.set(task.assigned, {
          completedStoryPoints: 0,
          totalStoryPoints: 0,
          productivity: 0,
        });
      }

      const developer = developers.get(task.assigned);
      if (task.done) {
        developer.completedStoryPoints += task.storyPoints;
        totalCompletedStoryPoints += task.storyPoints;
      }
      developer.totalStoryPoints += task.storyPoints
      totalStoryPoints += task.storyPoints;

      developer.productivity = totalStoryPoints
          ? ((developer.completedStoryPoints / totalStoryPoints) * 100).toFixed(2)
          : 0;

      developers.set(task.assigned, developer);
    });
    setDeveloperProductivityData({
      labels: Array.from(developers.keys()),
      values: Array.from(developers.values()).map((v) => v.productivity),
    });
  }, [props.tasks]);

  // Chart data and options
  const chartColors = {
    background: ["#ff4d4d", "#ff1a1a", "#e60000", "#b30000", "#b30000", "#ffe5e5", "#ffb3b3", "#ff8080", "#800000", "#4d0000", "#1a0000"],
    text: "#FFFFFF",
  };

  const completedToPendingTasksPieData = {
    labels: ["Completadas", "Pendientes"],
    datasets: [
      {
        label: "Tareas",
        data: [completedCount, pendingCount],
        backgroundColor: chartColors.background,
      },
    ],
  };

  const storyPointsBarData = {
    labels: storyPointsData.labels.length > 0 ? storyPointsData.labels : [],
    datasets: [
      {
        label: "StoryPoints Finished Per Developer",
        data: storyPointsData.values.length > 0 ? storyPointsData.values : [],
        backgroundColor: "#FF6F61",
      },
    ],
  };

  const productivityPieData = {
    labels: developerProductivityData.labels.length > 0 ? developerProductivityData.labels : [],
    datasets: [
      {
        label: "Productivity Per Developer",
        data: developerProductivityData.values.length > 0 ? developerProductivityData.values : [],
        backgroundColor: chartColors.background,
      },
    ],
  };

  const lineData = {
    labels: completedTasksSprintData.labels.length > 0 ? completedTasksSprintData.labels : [],
    datasets: [
      {
        label: "Finished Tasks Per Sprint",
        data: completedTasksSprintData.values.length > 0 ? completedTasksSprintData.values : [],
        backgroundColor: "rgba(248, 0, 0, 0.5)",
        fill: true,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: chartColors.text,
        },
      },
      title: {
        display: false,
        color: chartColors.text,
      },
    },
    scales: {
      x: {
        ticks: {
          color: chartColors.text,
        },
      },
      y: {
        ticks: {
          color: chartColors.text,
        },
      },
    },
  };

  return (
      <div className="graphs-container">
        <div style={{ display: "flex", gap: "40px", justifyContent: "center", alignItems: "center", marginTop: "30px" }}>
          <div>
            <h3 style={{ color: chartColors.text }}>Pending Tasks vs Completed Tasks</h3>
            <div style={{ height: "400px" }}>
              <Pie data={completedToPendingTasksPieData} options={chartOptions} />
            </div>
          </div>
          <div>
            <h3 style={{ color: chartColors.text }}>StoryPoints Finished Per Developer</h3>
            <div style={{ height: "400px" }}>
              <Bar data={storyPointsBarData} options={chartOptions} />
            </div>
          </div>
        </div>
        <div>
          <h3 style={{ color: chartColors.text }}>Tasks Finished Per Sprint</h3>
          <div style={{ height: "400px" }}>
            <Line data={lineData} options={chartOptions} />
          </div>
        </div>
        <div>
          <h3 style={{ color: chartColors.text }}>Developer Productivity</h3>
          <div style={{ height: "400px" }}>
            <Pie data={productivityPieData} options={chartOptions} />
          </div>
        </div>
      </div>
  );
}

export default Estadisticas;
