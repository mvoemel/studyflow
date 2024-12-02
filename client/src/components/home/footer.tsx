import { GithubIcon } from "../icons/github";
import { ZhawIcon } from "../icons/zhaw";

const Footer = () => {
  return (
    <footer className="mt-20">
      <div className="custom-screen">
        <div className="mt-10 py-8 border-t border-muted items-center justify-between sm:flex">
          <p className="text-muted-foreground text-center">
            © 2024 Studyflow GmbH. No rights reserved.
          </p>
          <div className="flex items-center justify-center gap-x-6 text-muted-foreground mt-6 sm:mt-0">
            <a
              href="https://github.com/mvoemel/studyflow"
              target="_blank"
              aria-label="social media"
            >
              <GithubIcon />
            </a>
            <a
              href="https://www.zhaw.ch"
              target="_blank"
              aria-label="social media"
            >
              <ZhawIcon />
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export { Footer };
