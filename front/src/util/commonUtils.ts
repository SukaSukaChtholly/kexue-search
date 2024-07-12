export function setTitle(title?: string) {
  if (!title) {
    document.title = import.meta.env.VITE_API_WEB_NAME;
    return;
  }
  document.title = title + '-' + import.meta.env.VITE_API_WEB_NAME;
}