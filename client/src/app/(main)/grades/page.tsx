import { GradesView } from "@/components/grades";

const GradesPage = () => {
  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      {/* <h1 className="text-3xl font-semibold">Grades</h1> */}
      <GradesView />
    </div>
  );
};

export default GradesPage;
