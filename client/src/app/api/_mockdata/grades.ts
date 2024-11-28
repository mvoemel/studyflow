const mockGrades = [
  {
    semesterId: "semester-21",
    semesterName: "1. Semester",
    modules: [
      {
        moduleId: "module-1",
        moduleName: "Algorithms",
        grades: [
          {
            id: "grade-1",
            name: "Zwischenprüfung",
            percentage: 0.25,
            value: 5.5,
          },
          { id: "grade-2", name: "SEP", percentage: 0.75, value: 5 },
        ],
      },
      {
        moduleId: "module-2",
        moduleName: "Mathematics 1",
        grades: [
          { id: "grade-3", name: "Projekt", percentage: 0.35, value: 4.5 },
          { id: "grade-4", name: "Aufgabe", percentage: 0.65, value: 3.5 },
        ],
      },
    ],
  },
  {
    semesterId: "semester-22",
    semesterName: "2. Semester",
    modules: [
      {
        moduleId: "module-3",
        moduleName: "Datastructure",
        grades: [
          {
            id: "grade-5",
            name: "Prüfung 1",
            percentage: 0.3,
            value: 4.9,
          },
          { id: "grade-6", name: "Prüfung 2", percentage: 0.7, value: 4.5 },
        ],
      },
      {
        moduleId: "module-4",
        moduleName: "Mathematics 2",
        grades: [
          { id: "grade-7", name: "Projekt", percentage: 0.35, value: 4.5 },
          { id: "grade-8", name: "Aufgabe", percentage: 0.65, value: 3.5 },
        ],
      },
    ],
  },
  {
    semesterId: "semester-23",
    semesterName: "3. Semester",
    modules: [
      {
        moduleId: "module-5",
        moduleName: "Communication",
        grades: [],
      },
    ],
  },
  {
    semesterId: "semester-24",
    semesterName: "4. Semester",
    modules: [],
  },
];

export { mockGrades };
