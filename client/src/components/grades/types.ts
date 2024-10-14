type Module = {
  name: string;
  ects: number;
  grade: number | string;
};

type Semester = {
  name: string;
  modules: Module[];
  ects: number;
  grade: number | string;
};

type Degree = {
  name: string;
  semesters: Semester[];
  totalEcts: number;
  totalGrade: number;
};

export { type Module, type Semester, type Degree };
