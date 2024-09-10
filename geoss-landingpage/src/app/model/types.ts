export type LinkData = {
  text: string;
  href: string;
};

export type Feature = {
  icon: any;
  text: string;
  href: string;
};

export type NewsTileData = {
  img: string;
  date: string;
  text: string;
  id: string;
};

export type PaginationData = {
  size: number;
  totalElements: number;
  totalPages: number;
  currentPage: number;
};

export type ProviderData = {
  name: string;
  id: string;
  description: string;
  title: string;
  approval_status: string;
  state: string;
  image_url: string;
  extras: any[];
};
