/**
 * Generic tree node for dynamic JSON-driven trees.
 */
export interface TreeNode<TMeta = unknown> {
  id: string;
  label: string;
  children?: TreeNode<TMeta>[];
  /** Optional payload — e.g. API id, icon key, route */
  meta?: TMeta;
}
