import {
  ChartColumnIcon,
  ChartNoAxesCombinedIcon,
  MonitorIcon,
  MoonIcon,
  UserRoundIcon,
} from "lucide-react";

const featuresList = [
  {
    icon: <ChartNoAxesCombinedIcon />,
    title: "Personalized Study Plans",
    desc: "Custom-generated plans tailored to your coursework, deadlines, and goals.",
  },
  {
    icon: <UserRoundIcon />,
    title: "Dynamic Adjustments",
    desc: "Update your plan in real-time based on your progress and changing schedules.",
  },
  {
    icon: <ChartColumnIcon />,
    title: "Grade Tracking",
    desc: "Monitor your performance across subjects and stay on top of your academic goals.",
  },
  {
    icon: <MonitorIcon />,
    title: "Cross-Platform Accessibility",
    desc: "Access your plans and grades from any device, anywhere.",
  },
  {
    icon: <MoonIcon />,
    title: "Dark Mode Support",
    desc: "Reduce eye strain during late-night study sessions with an aesthetic and functional dark mode.",
  },
];

const testimonialsList = [
  {
    avatar: "https://i.pravatar.cc/100?img=1",
    name: "Sarah",
    title: "Business Major",
    quote:
      "This app has been a lifesaver! I feel more organized and confident in my studies than ever before.",
  },
  {
    avatar: "https://i.pravatar.cc/100?img=2",
    name: "Alex",
    title: "Computer Science Student",
    quote:
      "Finally, a study plan that works for me! It adjusts to my pace and keeps me on track.",
  },
  {
    avatar: "https://i.pravatar.cc/100?img=3",
    name: "Maya",
    title: "Psychology Major",
    quote:
      "The grade tracking feature is a game-changer. I always know where I stand and what I need to focus on.",
  },
  {
    avatar: "https://i.pravatar.cc/100?img=4",
    name: "Liam",
    title: "Engineering Student",
    quote:
      "I can’t imagine going through a semester without this. It’s like having a personal academic coach.",
  },
  {
    avatar: "https://i.pravatar.cc/100?img=5",
    name: "Emma",
    title: "Literature Major",
    quote:
      "The best part is how stress-free it makes planning. My GPA has never been higher!",
  },
];

const faqsList = [
  {
    q: "How does the algorithm create my personalized study plan?",
    a: "Our algorithm considers your coursework, deadlines, personal study preferences, and workload to create a customized plan that maximizes efficiency and minimizes stress.",
  },
  {
    q: "Can I edit the study plan after it’s generated?",
    a: "Absolutely! You can tweak the plan to fit your schedule, and the algorithm will adjust dynamically to accommodate changes.",
  },
  {
    q: "How does grade tracking work?",
    a: "Simply input your grades as you receive them, and our system will calculate your overall performance, providing insights on where to improve.",
  },
  {
    q: "Is my data secure?",
    a: "Yes, your data is encrypted and stored securely. We prioritize your privacy and comply with all data protection regulations.",
  },
  {
    q: "What devices can I use this application on?",
    a: "Our app is available on desktop, tablets, and smartphones, ensuring you can access it wherever you are.",
  },
  {
    q: "Does this work for part-time students?",
    a: "Definitely! The app is flexible and can accommodate any academic schedule, full-time or part-time.",
  },
];

export { featuresList, testimonialsList, faqsList };
